<template>
  <section class="card">
    <div class="panel-head">
      <h3 class="title">基础数据管理</h3>
      <p class="muted">每类数据独立管理：新增 + 查看详情，信息更聚焦。</p>
    </div>

    <div class="stack-sections" style="margin-top: 12px;">
      <section class="master-item">
        <div class="master-item-head">
          <div>
            <h4 class="sub-title">仓库管理</h4>
            <p class="muted">维护仓库基础信息，并可跳转查看所有仓库。</p>
          </div>
          <div class="master-actions">
            <button class="btn" @click="toggle('warehouse')">{{ openPanel === 'warehouse' ? '收起新增仓库' : '新增仓库' }}</button>
            <button class="btn btn-primary" @click="go('/master-data/warehouse')">查看仓库</button>
          </div>
        </div>
        <div v-if="openPanel === 'warehouse'" class="master-form">
          <div class="grid grid-3">
            <input class="input" v-model="warehouseForm.name" placeholder="仓库名称" />
            <input class="input" v-model="warehouseForm.address" placeholder="仓库地址" />
            <input class="input" type="number" min="1" v-model.number="warehouseForm.capacity" placeholder="仓库容量" />
          </div>
          <div class="master-submit"><button class="btn btn-primary" @click="saveWarehouse">保存仓库</button></div>
        </div>
      </section>

      <section class="master-item">
        <div class="master-item-head">
          <div>
            <h4 class="sub-title">SKU管理</h4>
            <p class="muted">维护商品主数据、单位和体积重量参数。</p>
          </div>
          <div class="master-actions">
            <button class="btn" @click="toggle('sku')">{{ openPanel === 'sku' ? '收起新增SKU' : '新增SKU' }}</button>
            <button class="btn btn-primary" @click="go('/master-data/sku')">查看SKU</button>
          </div>
        </div>
        <div v-if="openPanel === 'sku'" class="master-form">
          <div class="grid grid-3">
            <input class="input" v-model="skuForm.code" placeholder="SKU编码" />
            <input class="input" v-model="skuForm.name" placeholder="SKU名称" />
            <input class="input" v-model="skuForm.category" placeholder="分类" />
            <input class="input" v-model="skuForm.unit" placeholder="单位（件/箱/托）" />
            <div class="input-group">
              <input class="input" type="number" min="0" step="0.01" v-model.number="skuForm.weight" placeholder="重量" />
              <span class="input-addon">kg</span>
            </div>
            <div class="input-group">
              <input class="input" type="number" min="0" step="0.01" v-model.number="skuForm.volume" placeholder="体积" />
              <span class="input-addon">m³</span>
            </div>
          </div>
          <div class="master-submit"><button class="btn btn-primary" @click="saveSku">保存SKU</button></div>
        </div>
      </section>

      <section class="master-item">
        <div class="master-item-head">
          <div>
            <h4 class="sub-title">库位管理</h4>
            <p class="muted">维护库位编码、容量和承重，支持按仓库归属管理。</p>
          </div>
          <div class="master-actions">
            <button class="btn" @click="toggle('location')">{{ openPanel === 'location' ? '收起新增库位' : '新增库位' }}</button>
            <button class="btn btn-primary" @click="go('/master-data/location')">查看库位</button>
          </div>
        </div>
        <div v-if="openPanel === 'location'" class="master-form">
          <div class="grid grid-3">
            <select class="select" v-model="locationForm.warehouseId">
              <option :value="null">选择仓库</option>
              <option v-for="w in warehouses" :key="w.id" :value="w.id">{{ w.name }}</option>
            </select>
            <input class="input" v-model="locationForm.code" placeholder="库位编码（A-01-01）" />
            <div class="input-group">
              <input class="input" type="number" min="1" v-model.number="locationForm.maxCapacity" placeholder="最大容量" />
              <span class="input-addon">件</span>
            </div>
            <div class="input-group">
              <input class="input" type="number" min="1" step="0.1" v-model.number="locationForm.maxWeight" placeholder="最大承重" />
              <span class="input-addon">kg</span>
            </div>
          </div>
          <div class="master-submit"><button class="btn btn-primary" @click="saveLocation">保存库位</button></div>
        </div>
      </section>
    </div>
  </section>
</template>

<script setup>
import { inject, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { locationApi, skuApi, warehouseApi } from '@/api'

const router = useRouter()
const toast = inject('toast')

const warehouses = ref([])
const openPanel = ref('warehouse')

const warehouseForm = reactive({ name: '', address: '', capacity: 1000 })
const skuForm = reactive({ code: '', name: '', category: '', unit: '件', weight: 1, volume: 1 })
const locationForm = reactive({ warehouseId: '', code: '', maxCapacity: 500, maxWeight: 200 })

const go = (path) => router.push(path)
const toggle = (panel) => {
  openPanel.value = openPanel.value === panel ? '' : panel
}

async function loadWarehouses() {
  const list = await warehouseApi.list()
  warehouses.value = (list || []).map((w) => ({ ...w, id: String(w.id) }))
}

async function saveWarehouse() {
  if (!warehouseForm.name || !warehouseForm.address) {
    toast?.error('请完整填写仓库信息')
    return
  }
  await warehouseApi.upsert({ ...warehouseForm })
  toast?.success('仓库保存成功')
  warehouseForm.name = ''
  warehouseForm.address = ''
  await loadWarehouses()
}

async function saveSku() {
  if (!skuForm.code || !skuForm.name || !skuForm.unit) {
    toast?.error('请填写SKU编码、名称和单位')
    return
  }
  await skuApi.upsert({ ...skuForm })
  toast?.success('SKU保存成功')
  skuForm.code = ''
  skuForm.name = ''
}

async function saveLocation() {
  if (!locationForm.warehouseId || !locationForm.code) {
    toast?.error('请先选择仓库并填写库位编码')
    return
  }
  await locationApi.upsert({ ...locationForm })
  toast?.success('库位保存成功')
  locationForm.code = ''
}

onMounted(loadWarehouses)
</script>
