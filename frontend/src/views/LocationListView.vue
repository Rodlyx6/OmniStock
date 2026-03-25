<template>
  <section class="card">
    <div class="detail-head">
      <button class="btn btn-back" @click="goBack">← 返回基础数据</button>
      <h3 class="title">库位详情列表</h3>
      <button class="btn" @click="reload">刷新</button>
    </div>

    <div class="table-wrap" style="margin-top: 10px;">
      <table>
        <thead>
          <tr><th>库位编码</th><th>仓库</th><th>最大容量</th><th>最大承重(kg)</th><th>当前容量</th><th>当前重量(kg)</th></tr>
        </thead>
        <tbody>
          <tr v-for="l in rows" :key="l.id">
            <td>{{ l.code }}</td>
            <td>{{ l.warehouseName }}</td>
            <td>{{ l.maxCapacity }}</td>
            <td>{{ l.maxWeight }}</td>
            <td>{{ l.currentCapacity }}</td>
            <td>{{ l.currentWeight }}</td>
          </tr>
          <tr v-if="rows.length === 0"><td colspan="6" class="muted">暂无库位数据</td></tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { inject, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { inventoryApi, locationApi, skuApi, warehouseApi } from '@/api'

const router = useRouter()
const toast = inject('toast')
const rows = ref([])

const goBack = () => router.push('/master-data')
const toId = (v) => (v === null || v === undefined ? '' : String(v))

const reload = async () => {
  try {
    const [locationList, warehouseList, skuList, inventoryList] = await Promise.all([
      locationApi.list(),
      warehouseApi.list(),
      skuApi.list(),
      inventoryApi.list()
    ])

    const warehouseNameMap = Object.fromEntries((warehouseList || []).map((w) => [toId(w.id), w.name]))
    const skuWeightMap = Object.fromEntries((skuList || []).map((s) => [toId(s.id), Number(s.weight || 0)]))

    const locationStats = {}
    for (const row of inventoryList || []) {
      const locationId = toId(row.locationId)
      const quantity = Number(row.quantity || 0)
      const skuWeight = skuWeightMap[toId(row.skuId)] || 0
      if (!locationStats[locationId]) {
        locationStats[locationId] = { capacity: 0, weight: 0 }
      }
      locationStats[locationId].capacity += quantity
      locationStats[locationId].weight += quantity * skuWeight
    }

    rows.value = (locationList || []).map((l) => {
      const id = toId(l.id)
      const stat = locationStats[id] || { capacity: 0, weight: 0 }
      return {
        ...l,
        id,
        warehouseId: toId(l.warehouseId),
        warehouseName: warehouseNameMap[toId(l.warehouseId)] || '-',
        currentCapacity: stat.capacity,
        currentWeight: Number(stat.weight.toFixed(2))
      }
    })
    toast?.success('库位列表已刷新')
  } catch (e) {
    toast?.error(e?.message || '刷新失败')
  }
}

onMounted(reload)
</script>
