<template>
  <section class="card">
    <div class="detail-head">
      <button class="btn btn-back" @click="goBack">← 返回基础数据</button>
      <h3 class="title">SKU详情列表</h3>
      <button class="btn" @click="reload">刷新</button>
    </div>

    <div class="table-wrap" style="margin-top: 10px;">
      <table>
        <thead>
          <tr><th>ID</th><th>SKU编码</th><th>名称</th><th>分类</th><th>单位</th><th>重量(kg)</th><th>体积(m³)</th></tr>
        </thead>
        <tbody>
          <tr v-for="s in rows" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.code }}</td>
            <td>{{ s.name }}</td>
            <td>{{ s.category || '-' }}</td>
            <td>{{ s.unit || '-' }}</td>
            <td>{{ s.weight }}</td>
            <td>{{ s.volume }}</td>
          </tr>
          <tr v-if="rows.length === 0"><td colspan="7" class="muted">暂无SKU数据</td></tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { skuApi } from '@/api'

const router = useRouter()
const rows = ref([])

const goBack = () => router.push('/master-data')

const reload = async () => {
  rows.value = await skuApi.list()
}

onMounted(reload)
</script>
