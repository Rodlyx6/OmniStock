<template>
  <div class="layout">
    <aside class="sider">
      <div class="logo">
        <span class="logo-mark">OS</span>
        <div>
          <p class="logo-title">OmniStock</p>
          <p class="logo-sub">智能仓储控制台</p>
        </div>
      </div>
      <nav class="menu">
        <button v-for="item in menus" :key="item.path" class="menu-item" :class="{ active: $route.path === item.path }" @click="go(item.path)">
          {{ item.label }}
        </button>
      </nav>
    </aside>

    <section class="main">
      <header class="header">
        <h1 class="header-title">OmniStock 智能仓储运营平台</h1>
        <p class="header-sub">实时协同 · 智能分析 · 精准决策</p>
      </header>

      <main class="content">
        <router-view />
      </main>
    </section>

    <ToastMessage :visible="toast.visible.value" :message="toast.message.value" :type="toast.type.value" />
  </div>
</template>

<script setup>
import { provide } from 'vue'
import { useRouter } from 'vue-router'
import ToastMessage from '@/components/ToastMessage.vue'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const toast = useToast()
provide('toast', toast)

const menus = [
  { path: '/dashboard', label: '工作台' },
  { path: '/master-data', label: '基础数据' },
  { path: '/inbound', label: '入库作业' },
  { path: '/outbound', label: '出库作业' },
  { path: '/inventory', label: '库存查询' },
  { path: '/flow', label: '库存流水' }
]

const go = (path) => router.push(path)
</script>
