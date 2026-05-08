import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '@/views/DashboardView.vue'
import MasterDataView from '@/views/MasterDataView.vue'
import WarehouseListView from '@/views/WarehouseListView.vue'
import SkuListView from '@/views/SkuListView.vue'
import LocationListView from '@/views/LocationListView.vue'
import InboundView from '@/views/InboundView.vue'
import OutboundView from '@/views/OutboundView.vue'
import InventoryView from '@/views/InventoryView.vue'
import FlowView from '@/views/FlowView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import AdminLoginView from '@/views/AdminLoginView.vue'
import ForgotPasswordView from '@/views/ForgotPasswordView.vue'
import UserManagementView from '@/views/UserManagementView.vue'

const routes = [
  { path: '/login', component: LoginView, meta: { public: true } },
  { path: '/admin/login', component: AdminLoginView, meta: { public: true } },
  { path: '/register', component: RegisterView, meta: { public: true } },
  { path: '/forgot-password', component: ForgotPasswordView, meta: { public: true } },
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: DashboardView },
  { path: '/master-data', component: MasterDataView },
  { path: '/master-data/warehouse', component: WarehouseListView },
  { path: '/master-data/sku', component: SkuListView },
  { path: '/master-data/location', component: LocationListView },
  { path: '/inbound', component: InboundView },
  { path: '/outbound', component: OutboundView },
  { path: '/inventory', component: InventoryView },
  { path: '/flow', component: FlowView },
  { path: '/users', component: UserManagementView, meta: { roles: ['ROLE_ADMIN'] } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  
  if (!to.meta.public && !token) {
    next('/login')
  } else if (to.meta.roles && !to.meta.roles.some(role => user.roles?.includes(role))) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
