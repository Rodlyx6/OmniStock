<template>
  <section class="card">
    <div class="detail-head">
      <button class="btn btn-back" @click="goBack">← 返回基础数据</button>
      <h3 class="title">仓库详情列表</h3>
      <button class="btn" @click="reload">刷新</button>
    </div>

    <div class="table-wrap" style="margin-top: 10px;">
      <table>
        <thead>
          <tr><th>ID</th><th>仓库名称</th><th>地址</th><th>总容量</th><th>已用容量</th><th>更新时间</th></tr>
        </thead>
        <tbody>
          <tr v-for="w in rows" :key="w.id">
            <td>{{ w.id }}</td>
            <td>{{ w.name }}</td>
            <td>{{ w.address }}</td>
            <td>{{ w.capacity }}</td>
            <td>{{ w.currentUsage }}</td>
            <td>{{ fmt(w.updatedTime) }}</td>
          </tr>
          <tr v-if="rows.length === 0"><td colspan="6" class="muted">暂无仓库数据</td></tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { warehouseApi } from '@/api'

const router = useRouter()
const rows = ref([])
const fmt = (v) => (v ? new Date(v).toLocaleString() : '-')

const goBack = () => router.push('/master-data')

const reload = async () => {
  rows.value = await warehouseApi.list()
}

onMounted(reload)
</script>
