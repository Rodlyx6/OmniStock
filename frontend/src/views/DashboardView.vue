<template>
  <section class="dashboard-hero card">
    <div>
      <h2 class="dashboard-title">运营驾驶舱</h2>
      <p class="dashboard-sub">以实时库存、库容效率与动销表现为核心，支撑采购与补货决策。</p>
    </div>
    <button class="btn btn-primary" @click="reload">刷新分析</button>
  </section>

  <section class="grid grid-4" style="margin-top: 12px;">
    <div class="kpi dash-kpi">
      <div class="kpi-label">仓库数</div>
      <div class="kpi-value">{{ stat.warehouseCount }}</div>
    </div>
    <div class="kpi dash-kpi">
      <div class="kpi-label">SKU 数</div>
      <div class="kpi-value">{{ stat.skuCount }}</div>
    </div>
    <div class="kpi dash-kpi">
      <div class="kpi-label">库存记录</div>
      <div class="kpi-value">{{ stat.inventoryCount }}</div>
    </div>
    <div class="kpi dash-kpi">
      <div class="kpi-label">库存总量</div>
      <div class="kpi-value">{{ stat.totalQty }}</div>
    </div>
  </section>

  <section class="grid grid-2" style="margin-top: 12px;">
    <article class="card chart-card">
      <div class="chart-head">
        <h3 class="title">ABC 分类（按动销量）</h3>
        <span class="muted">A高频 / B中频 / C低频</span>
      </div>
      <div class="pie-wrap">
        <div class="pie-chart" :style="{ background: pieGradient }"></div>
        <div class="pie-legend">
          <div v-for="item in abcPie" :key="item.label" class="legend-row">
            <span class="legend-dot" :style="{ background: item.color }"></span>
            <span>{{ item.label }}</span>
            <strong>{{ item.value }}</strong>
          </div>
        </div>
      </div>
    </article>

    <article class="card chart-card">
      <div class="chart-head">
        <h3 class="title">库容利用率（按仓库）</h3>
        <span class="muted">当前容量 / 最大容量</span>
      </div>
      <div class="bar-list">
        <div v-for="item in utilizationBars" :key="item.name" class="bar-row">
          <div class="bar-label">{{ item.name }}</div>
          <div class="bar-track">
            <div class="bar-fill" :style="{ width: `${item.percent}%` }"></div>
          </div>
          <div class="bar-value">{{ item.percent }}%</div>
        </div>
      </div>
    </article>
  </section>

  <section class="grid grid-2" style="margin-top: 12px;">
    <article class="card">
      <h3 class="title">库存预警（安全库存阈值）</h3>
      <div class="table-wrap" style="margin-top: 8px;">
        <table>
          <thead><tr><th>SKU</th><th>当前库存</th><th>安全库存</th><th>状态</th></tr></thead>
          <tbody>
            <tr v-for="row in lowStockRows" :key="row.skuId">
              <td>{{ row.skuName }}</td>
              <td>{{ row.qty }}</td>
              <td>{{ row.safeQty }}</td>
              <td><span class="tag tag-out">低库存</span></td>
            </tr>
            <tr v-if="lowStockRows.length === 0"><td colspan="4" class="muted">暂无低库存告警</td></tr>
          </tbody>
        </table>
      </div>
    </article>

    <article class="card">
      <h3 class="title">智能推荐库位（ABC）</h3>
      <div class="table-wrap" style="margin-top: 8px;">
        <table>
          <thead><tr><th>SKU</th><th>等级</th><th>推荐仓库</th><th>推荐库位策略</th></tr></thead>
          <tbody>
            <tr v-for="row in recommendationRows" :key="row.skuId">
              <td>{{ row.skuName }}</td>
              <td>{{ row.level }}</td>
              <td>{{ row.warehouse }}</td>
              <td>{{ row.strategy }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </article>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { inventoryApi, locationApi, skuApi, warehouseApi } from '@/api'

const stat = reactive({ warehouseCount: 0, skuCount: 0, inventoryCount: 0, totalQty: 0 })
const abcPie = ref([])
const utilizationBars = ref([])
const lowStockRows = ref([])
const recommendationRows = ref([])

const pieGradient = computed(() => {
  if (abcPie.value.length === 0) return 'conic-gradient(#e2e8f0 0 100%)'
  const total = abcPie.value.reduce((s, i) => s + i.value, 0) || 1
  let start = 0
  const stops = abcPie.value.map((item) => {
    const span = (item.value / total) * 360
    const end = start + span
    const seg = `${item.color} ${start}deg ${end}deg`
    start = end
    return seg
  })
  return `conic-gradient(${stops.join(',')})`
})

async function reload() {
  const [warehouses, skus, inventory, locations, logs] = await Promise.all([
    warehouseApi.list(),
    skuApi.list(),
    inventoryApi.list(),
    locationApi.list(),
    inventoryApi.logs()
  ])

  stat.warehouseCount = warehouses.length
  stat.skuCount = skus.length
  stat.inventoryCount = inventory.length
  stat.totalQty = inventory.reduce((sum, row) => sum + Number(row.quantity || 0), 0)

  const skuNameMap = Object.fromEntries(skus.map((s) => [String(s.id), s.name]))
  const locationMap = Object.fromEntries(locations.map((l) => [String(l.id), l]))
  const warehouseNameMap = Object.fromEntries(warehouses.map((w) => [String(w.id), w.name]))

  const salesMap = {}
  for (const log of logs) {
    const skuId = String(log.skuId)
    salesMap[skuId] = (salesMap[skuId] || 0) + Math.abs(Number(log.changeQty || 0))
  }

  const ranked = Object.entries(salesMap)
    .map(([skuId, score]) => ({ skuId, score, skuName: skuNameMap[skuId] || skuId }))
    .sort((a, b) => b.score - a.score)

  const totalSkus = ranked.length || 1
  const aCut = Math.max(1, Math.ceil(totalSkus * 0.2))
  const bCut = Math.max(aCut + 1, Math.ceil(totalSkus * 0.5))

  const levelMap = {}
  ranked.forEach((item, idx) => {
    if (idx < aCut) levelMap[item.skuId] = 'A'
    else if (idx < bCut) levelMap[item.skuId] = 'B'
    else levelMap[item.skuId] = 'C'
  })

  const counts = { A: 0, B: 0, C: 0 }
  Object.values(levelMap).forEach((lv) => { counts[lv] += 1 })
  abcPie.value = [
    { label: 'A类', value: counts.A, color: '#1d4ed8' },
    { label: 'B类', value: counts.B, color: '#3b82f6' },
    { label: 'C类', value: counts.C, color: '#93c5fd' }
  ]

  const locationLoadByWarehouse = {}
  for (const row of inventory) {
    const location = locationMap[String(row.locationId)]
    if (!location) continue
    const wId = String(location.warehouseId)
    locationLoadByWarehouse[wId] = (locationLoadByWarehouse[wId] || 0) + Number(row.quantity || 0)
  }

  utilizationBars.value = warehouses.map((w) => {
    const used = locationLoadByWarehouse[String(w.id)] || 0
    const cap = Number(w.capacity || 1)
    const percent = Math.min(100, Math.round((used / cap) * 100))
    return { name: w.name, percent }
  })

  const skuQtyMap = {}
  for (const row of inventory) {
    const skuId = String(row.skuId)
    skuQtyMap[skuId] = (skuQtyMap[skuId] || 0) + Number(row.quantity || 0)
  }
  lowStockRows.value = Object.entries(skuQtyMap)
    .map(([skuId, qty]) => ({ skuId, skuName: skuNameMap[skuId] || skuId, qty, safeQty: 200 }))
    .filter((row) => row.qty < row.safeQty)
    .sort((a, b) => a.qty - b.qty)
    .slice(0, 8)

  const warehouseByUtilDesc = [...utilizationBars.value].sort((a, b) => a.percent - b.percent)
  const bestWarehouse = warehouseByUtilDesc[0]?.name || '-'
  recommendationRows.value = ranked.slice(0, 8).map((item) => {
    const level = levelMap[item.skuId] || 'C'
    return {
      skuId: item.skuId,
      skuName: item.skuName,
      level,
      warehouse: bestWarehouse,
      strategy: level === 'A' ? '近出货口库位优先' : level === 'B' ? '中区均衡存放' : '远区低频库位'
    }
  })
}

onMounted(reload)
</script>
