<template>
  <section class="card elevate">
    <div class="panel-head">
      <h3 class="title">出库作业</h3>
      <p class="muted">库位基于仓库过滤，避免同编码重复误选。</p>
    </div>

    <div class="grid grid-3" style="margin-top: 12px;">
      <SmartSuggest v-model="form.skuId" :options="skuOptions" placeholder="搜索SKU（如：iphone 15）" />
      <SmartSuggest v-model="form.warehouseId" :options="warehouseOptions" placeholder="搜索并选择仓库" />
      <SmartSuggest v-model="form.locationId" :options="locationOptions" placeholder="先选仓库，再选库位" />
    </div>

    <div class="grid grid-2" style="margin-top: 10px; align-items:end;">
      <div class="input-group">
        <input class="input" type="number" min="1" v-model.number="form.quantity" placeholder="出库数量" />
        <span class="input-addon">{{ selectedUnit }}</span>
      </div>
      <button class="btn btn-danger" :disabled="!form.skuId || !form.warehouseId || !form.locationId || form.quantity > availableQty" @click="submitOutbound">确认出库</button>
    </div>

    <div class="quick-hint" style="margin-top: 10px;">
      <span>单位：<strong>{{ selectedUnit }}</strong></span>
      <span>可出库：<strong>{{ availableQty }}</strong></span>
      <span>容量：<strong>{{ locationCapacityText }}</strong></span>
      <span>重量：<strong>{{ locationWeightText }}</strong></span>
      <span :class="form.quantity > availableQty ? 'danger-text' : 'ok-text'">{{ form.quantity > availableQty ? '数量超出可用库存' : '库存充足' }}</span>
    </div>
  </section>

  <section class="card" style="margin-top: 12px;">
    <h3 class="title">当前库存预览</h3>
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
  meta: `${warehouseNameMap.value[l.warehouseId] || ''} · 容量 ${l.currentCapacity || 0}/${l.maxCapacity || 0}`
})))

const selectedSku = computed(() => skus.value.find((s) => s.id === form.skuId))
const selectedLocation = computed(() => locations.value.find((l) => l.id === form.locationId))
const selectedUnit = computed(() => selectedSku.value?.unit || '件')

const availableQty = computed(() => {
  const hit = inventoryRows.value.find((r) => r.skuId === form.skuId && r.locationId === form.locationId)
  return hit?.quantity || 0
})

const locationCapacityText = computed(() => {
  if (!selectedLocation.value) return '-'
  const current = selectedLocation.value.currentCapacity || 0
  const max = selectedLocation.value.maxCapacity || 0
  return `${current}/${max} → ${Math.max(current - (form.quantity || 0), 0)}/${max}`
})

const locationWeightText = computed(() => {
  if (!selectedLocation.value || !selectedSku.value) return '-'
  const current = Number(selectedLocation.value.currentWeight || 0)
  const max = Number(selectedLocation.value.maxWeight || 0)
  const next = Math.max(current - Number(selectedSku.value.weight || 0) * Number(form.quantity || 0), 0)
  return `${current.toFixed(2)}/${max}kg → ${next.toFixed(2)}/${max}kg`
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

async function submitOutbound() {
  if (!form.skuId || !form.warehouseId || !form.locationId || !form.quantity) return toast?.error('请填写完整出库信息')
  if (form.quantity > availableQty.value) return toast?.error('出库数量超出可用库存')
  try {
    await inventoryApi.outbound({ operatorId: 1, items: [{ skuId: form.skuId, locationId: form.locationId, quantity: form.quantity }] })
    toast?.success('出库成功')
    await loadBaseData()
  } catch (e) {
    toast?.error(e?.message || '出库失败')
  }
}

onMounted(loadBaseData)
</script>
