<template>
  <section class="card elevate">
    <h3 class="title">库存流水</h3>
    <div class="grid grid-3" style="margin-top: 12px;">
      <SmartSuggest v-model="query.skuId" :options="skuOptions" placeholder="搜索SKU（模糊匹配）" />
      <SmartSuggest v-model="query.warehouseId" :options="warehouseOptions" placeholder="按仓库筛选流水" />
      <select class="select" v-model="query.changeType">
        <option value="">全部类型</option>
        <option value="INBOUND">INBOUND</option>
        <option value="OUTBOUND">OUTBOUND</option>
      </select>
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
          <tr><th>时间</th><th>类型</th><th>SKU</th><th>仓库</th><th>库位</th><th>变更数量</th><th>业务单号</th><th>操作人</th></tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.id">
            <td>{{ formatTime(row.createdTime) }}</td>
            <td><span class="tag" :class="row.changeType === 'INBOUND' ? 'tag-in' : 'tag-out'">{{ row.changeType }}</span></td>
            <td>{{ row.skuName }}</td>
            <td>{{ row.warehouseName }}</td>
            <td>{{ row.locationCode || '-' }}</td>
            <td>{{ row.changeQty }}</td>
            <td>{{ row.bizId }}</td>
            <td>{{ row.operatorId }}</td>
          </tr>
          <tr v-if="rows.length === 0"><td colspan="8" class="muted">暂无流水数据</td></tr>
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
const query = reactive({ skuId: '', warehouseId: '', changeType: '' })

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
    const result = await inventoryApi.logs({ skuId: query.skuId || undefined, changeType: query.changeType || undefined })
    const locationMap = Object.fromEntries(locations.value.map((l) => [l.id, l]))
    const warehouseMap = Object.fromEntries(warehouses.value.map((w) => [w.id, w.name]))

    rows.value = (result || [])
      .map((row) => {
        const location = locationMap[toId(row.locationId)]
        const warehouseId = location?.warehouseId || ''
        return {
          ...row,
          id: toId(row.id),
          skuId: toId(row.skuId),
          locationId: toId(row.locationId),
          warehouseId,
          warehouseName: warehouseMap[warehouseId] || '-'
        }
      })
      .filter((row) => !query.warehouseId || row.warehouseId === query.warehouseId)
  } catch (e) {
    toast?.error(e?.message || '流水查询失败')
  }
}

async function reset() {
  query.skuId = ''
  query.warehouseId = ''
  query.changeType = ''
  await search()
}

onMounted(async () => {
  await loadBase()
  await search()
})
</script>
