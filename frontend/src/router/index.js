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

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: DashboardView },
  { path: '/master-data', component: MasterDataView },
  { path: '/master-data/warehouse', component: WarehouseListView },
  { path: '/master-data/sku', component: SkuListView },
  { path: '/master-data/location', component: LocationListView },
  { path: '/inbound', component: InboundView },
  { path: '/outbound', component: OutboundView },
  { path: '/inventory', component: InventoryView },
  { path: '/flow', component: FlowView }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
