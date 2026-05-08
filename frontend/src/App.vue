<template>
  <div class="layout" :class="{ 'auth-layout': isAuthPage }">
    <aside v-if="!isAuthPage" class="sider">
      <div class="logo">
        <span class="logo-mark">OS</span>
        <div>
          <p class="logo-title">OmniStock</p>
          <p class="logo-sub">仓库控制台</p>
        </div>
      </div>
      <nav class="menu">
        <button v-for="item in filteredMenus" :key="item.path" class="menu-item" :class="{ active: $route.path === item.path }" @click="go(item.path)">
          {{ item.label }}
        </button>
      </nav>
    </aside>

    <section class="main">
      <header v-if="!isAuthPage" class="header">
        <div class="header-left">
          <h1 class="header-title">OmniStock 仓库管理系统</h1>
          <p class="header-sub">实时协同 · 智能分析 · 精准决策</p>
        </div>
        <div class="header-right" v-if="user">
          <div class="user-info">
            <div class="avatar">{{ user.username.charAt(0) }}</div>
            <span class="nickname">{{ user.username }}</span>
          </div>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </header>

      <main class="content">
        <router-view />
      </main>
    </section>

    <ToastMessage :visible="toast.visible.value" :message="toast.message.value" :type="toast.type.value" />
  </div>
</template>

<script setup>
import { provide, computed, ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import ToastMessage from '@/components/ToastMessage.vue'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const route = useRoute()
const toast = useToast()
provide('toast', toast)

const user = ref(null)

const updateUser = () => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  } else {
    user.value = null
  }
}

onMounted(updateUser)
watch(() => route.path, updateUser)

const isAuthPage = computed(() => ['/login', '/register', '/admin/login'].includes(route.path))

const menus = [
  { path: '/dashboard', label: '工作台' },
  { path: '/master-data', label: '基础数据' },
  { path: '/inbound', label: '入库作业' },
  { path: '/outbound', label: '出库作业' },
  { path: '/inventory', label: '库存查询' },
  { path: '/flow', label: '库存流水' },
  { path: '/users', label: '用户管理', roles: ['ROLE_ADMIN'] }
]

const filteredMenus = computed(() => {
  if (!user.value) return menus.filter(m => !m.roles)
  return menus.filter(m => !m.roles || m.roles.some(r => user.value.roles?.includes(r)))
})

const go = (path) => router.push(path)

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  user.value = null
  toast.success('已退出登录')
  router.push('/login')
}
</script>

<style>
/* 全局样式调整 */
.auth-layout {
  display: block !important;
  grid-template-columns: none !important;
}
.auth-layout .main {
  padding: 0 !important;
  width: 100%;
  height: 100vh;
}
.auth-layout .content {
  margin-top: 0 !important;
  height: 100%;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--panel);
  border: 1px solid var(--line);
  border-radius: 12px;
  padding: 16px 18px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.avatar {
  width: 32px;
  height: 32px;
  background: #2f54eb;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}
.nickname {
  color: #262626;
  font-weight: 500;
}
.logout-btn {
  padding: 6px 12px;
  border: 1px solid #d9d9d9;
  background: transparent;
  color: #595959;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}
.logout-btn:hover {
  color: #ff4d4f;
  border-color: #ff4d4f;
}
</style>
