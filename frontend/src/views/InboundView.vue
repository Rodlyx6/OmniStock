<template>
  <section class="card elevate">
    <div class="panel-head">
      <h3 class="title">入库作业</h3>
      <p class="muted">先选仓库，再选库位；SKU 搜索仅保留高相关结果。</p>
    </div>

    <div class="grid grid-3" style="margin-top: 12px;">
      <SmartSuggest v-model="form.skuId" :options="skuOptions" placeholder="搜索SKU（如：iphone 15）" />
      <SmartSuggest v-model="form.warehouseId" :options="warehouseOptions" placeholder="搜索并选择仓库" />
      <SmartSuggest v-model="form.locationId" :options="locationOptions" placeholder="先选仓库，再选库位" />
    </div>

    <div class="grid grid-2" style="margin-top: 10px; align-items:end;">
      <div class="input-group">
        <input class="input" type="number" min="1" v-model.number="form.quantity" placeholder="入库数量" />
        <span class="input-addon">{{ selectedUnit }}</span>
      </div>
      <button class="btn btn-primary" :disabled="isCapacityExceeded || isWeightExceeded" @click="submitInbound">确认入库</button>
    </div>

    <div class="quick-hint" style="margin-top: 10px;">
      <span>单位：<strong>{{ selectedUnit }}</strong></span>
      <span>入库后数量：<strong>{{ expectedQty }}</strong></span>
      <span v-if="isCapacityExceeded" class="danger-text">提示：已超库位最大容量，无法入库</span>
      <span v-if="isWeightExceeded" class="danger-text">提示：已超库位最大承重，无法入库</span>
      <span v-if="!isCapacityExceeded && !isWeightExceeded" class="ok-text">校验通过，可安全入库</span>
    </div>
  </section>

  <section class="card" style="margin-top: 12px;">
    <h3 class="title">入库后库存预览</h3>
    <div class="table-wrap" style="margin-top: 8px;">
      <table><thead><tr><th>SKU</th><th>仓库</th><th>库位</th><th>数量</th><th>单位</th><th>更新时间</th></tr></thead><tbody>
        <tr v-for="item in inventoryRows" :key="item.id">
          <td>{{ item.skuName }}</td><td>{{ warehouseNameMap[item.warehouseId] || '-' }}</td><td>{{ item.locationCode }}</td><td>{{ item.quantity }}</td>
          <td>{{ unitMap[item.skuId] || '-' }}</td><td>{{ formatTime(item.updatedTime) }}</td>
        </tr>
      </tbody></table>
    </div>
  </section>
</template>

<script setup>
import { computed, inject, onMounted, reactive, ref, watch } from 'vue'
import { inventoryApi, locationApi, skuApi, warehouseApi } from '@/api'
import SmartSuggest from '@/components/SmartSuggest.vue'

const toast = inject('toast')
const skus = ref([])
const warehouses = ref([])
const locations = ref([])
const inventoryRows = ref([])
const unitMap = ref({})
const warehouseNameMap = ref({})

const form = reactive({ skuId: '', warehouseId: '', locationId: '', quantity: 1 })

const toId = (v) => (v === null || v === undefined ? '' : String(v))
const formatTime = (time) => (time ? new Date(time).toLocaleString() : '-')

const skuOptions = computed(() => skus.value.map((s) => ({ id: s.id, label: s.name, meta: s.code || '' })))
const warehouseOptions = computed(() => warehouses.value.map((w) => ({ id: w.id, label: w.name, meta: w.address || '' })))
const filteredLocations = computed(() => {
  if (!form.warehouseId) return []
  return locations.value.filter((l) => l.warehouseId === form.warehouseId)
})
const locationOptions = computed(() => filteredLocations.value.map((l) => ({
  id: l.id,
  label: l.code,
  meta: `${warehouseNameMap.value[l.warehouseId] || ''}`
})))

const selectedSku = computed(() => skus.value.find((s) => s.id === form.skuId))
const selectedLocation = computed(() => locations.value.find((l) => l.id === form.locationId))
const selectedUnit = computed(() => selectedSku.value?.unit || '件')

const expectedQty = computed(() => {
  const hit = inventoryRows.value.find((row) => row.skuId === form.skuId && row.locationId === form.locationId)
  return (hit?.quantity || 0) + (form.quantity || 0)
})

const nextCapacity = computed(() => {
  if (!selectedLocation.value) return 0
  return Number(selectedLocation.value.currentCapacity || 0) + Number(form.quantity || 0)
})

const nextWeight = computed(() => {
  if (!selectedLocation.value || !selectedSku.value) return 0
  const current = Number(selectedLocation.value.currentWeight || 0)
  const delta = Number(selectedSku.value.weight || 0) * Number(form.quantity || 0)
  return current + delta
})

const isCapacityExceeded = computed(() => {
  if (!selectedLocation.value) return false
  return nextCapacity.value > Number(selectedLocation.value.maxCapacity || 0)
})

const isWeightExceeded = computed(() => {
  if (!selectedLocation.value) return false
  return nextWeight.value > Number(selectedLocation.value.maxWeight || 0)
})

watch(() => form.warehouseId, () => {
  form.locationId = ''
})

async function loadBaseData() {
  const [skuList, warehouseList, locationList, inventoryList] = await Promise.all([
    skuApi.list(),
    warehouseApi.list(),
    locationApi.list(),
    inventoryApi.list()
  ])
  skus.value = (skuList || []).map((s) => ({ ...s, id: toId(s.id) }))
  warehouses.value = (warehouseList || []).map((w) => ({ ...w, id: toId(w.id) }))
  locations.value = (locationList || []).map((l) => ({ ...l, id: toId(l.id), warehouseId: toId(l.warehouseId) }))

  const locMap = Object.fromEntries(locations.value.map((l) => [l.id, l]))
  inventoryRows.value = (inventoryList || [])
    .map((r) => ({ ...r, id: toId(r.id), skuId: toId(r.skuId), locationId: toId(r.locationId), warehouseId: locMap[toId(r.locationId)]?.warehouseId || '' }))
    .filter((r) => (r.quantity || 0) > 0)

  unitMap.value = Object.fromEntries(skus.value.map((s) => [s.id, s.unit || '件']))
  warehouseNameMap.value = Object.fromEntries(warehouses.value.map((w) => [w.id, w.name]))
}

async function submitInbound() {
  if (!form.skuId || !form.warehouseId || !form.locationId || !form.quantity) return toast?.error('请填写完整入库信息')
  if (isCapacityExceeded.value) return toast?.error('已超库位最大容量，无法入库')
  if (isWeightExceeded.value) return toast?.error('已超库位最大承重，无法入库')
  try {
    await inventoryApi.inbound({ operatorId: 1, items: [{ skuId: form.skuId, locationId: form.locationId, quantity: form.quantity }] })
    toast?.success('入库成功')
    await loadBaseData()
  } catch (e) {
    toast?.error(e?.message || '入库失败')
  }
}

onMounted(loadBaseData)
</script>
