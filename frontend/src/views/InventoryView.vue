<template>
  <section class="card elevate">
    <h3 class="title">库存查询</h3>
    <div class="grid grid-2" style="margin-top: 12px;">
      <SmartSuggest v-model="query.skuId" :options="skuOptions" placeholder="搜索SKU（大小写不敏感，支持模糊匹配）" />
      <SmartSuggest v-model="query.warehouseId" :options="warehouseOptions" placeholder="按仓库查询库存" />
    </div>

    <div style="margin-top: 10px; display:flex; gap:8px;">
      <button class="btn btn-primary" @click="search">查询</button>
      <button class="btn" @click="reset">重置</button>
    </div>
  </section>

  <section class="card" style="margin-top: 12px;">
    <div class="table-wrap">
      <table>
        <thead>
          <tr><th>SKU</th><th>仓库</th><th>库位</th><th>数量</th><th>更新时间</th></tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ row.skuName }}</td>
            <td>{{ row.warehouseName }}</td>
            <td>{{ row.locationCode }}</td>
            <td>{{ row.quantity }}</td>
            <td>{{ formatTime(row.updatedTime) }}</td>
          </tr>
          <tr v-if="rows.length === 0"><td colspan="5" class="muted">暂无数据</td></tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, inject, onMounted, reactive, ref } from 'vue'
import { inventoryApi, locationApi, skuApi, warehouseApi } from '@/api'
import SmartSuggest from '@/components/SmartSuggest.vue'

const toast = inject('toast')

const skus = ref([])
const warehouses = ref([])
const locations = ref([])
const rows = ref([])
const query = reactive({ skuId: '', warehouseId: '' })

const formatTime = (time) => (time ? new Date(time).toLocaleString() : '-')
const toId = (v) => (v === null || v === undefined ? '' : String(v))

const skuOptions = computed(() => skus.value.map((s) => ({ id: s.id, label: s.name, meta: s.code || '' })))
const warehouseOptions = computed(() => warehouses.value.map((w) => ({ id: w.id, label: w.name, meta: w.address || '' })))

async function loadBase() {
  const [skuList, warehouseList, locationList] = await Promise.all([skuApi.list(), warehouseApi.list(), locationApi.list()])
  skus.value = (skuList || []).map((s) => ({ ...s, id: toId(s.id) }))
  warehouses.value = (warehouseList || []).map((w) => ({ ...w, id: toId(w.id) }))
  locations.value = (locationList || []).map((l) => ({ ...l, id: toId(l.id), warehouseId: toId(l.warehouseId) }))
}

async function search() {
  try {
    const result = await inventoryApi.list({ skuId: query.skuId || undefined })
    const locationMap = Object.fromEntries(locations.value.map((l) => [l.id, l]))
    const warehouseMap = Object.fromEntries(warehouses.value.map((w) => [w.id, w.name]))

    rows.value = (result || [])
      .map((row) => {
        const location = locationMap[toId(row.locationId)]
        return {
          ...row,
          id: toId(row.id),
          skuId: toId(row.skuId),
          locationId: toId(row.locationId),
          warehouseId: location?.warehouseId || '',
          warehouseName: warehouseMap[location?.warehouseId] || '-'
        }
      })
      .filter((row) => (row.quantity || 0) > 0)
      .filter((row) => !query.warehouseId || row.warehouseId === query.warehouseId)
  } catch (e) {
    toast?.error(e?.message || '查询失败')
  }
}

async function reset() {
  query.skuId = ''
  query.warehouseId = ''
  await search()
}

onMounted(async () => {
  await loadBase()
  await search()
})
</script>
