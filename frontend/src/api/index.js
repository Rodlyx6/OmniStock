import http from './http'

export const warehouseApi = {
  list: () => http.get('/warehouse'),
  upsert: (data) => http.post('/warehouse', data)
}

export const skuApi = {
  list: () => http.get('/sku'),
  upsert: (data) => http.post('/sku', data)
}

export const locationApi = {
  list: () => http.get('/location'),
  listByWarehouse: (warehouseId) => http.get('/location/warehouse', { params: { warehouseId } }),
  upsert: (data) => http.post('/location', data)
}

export const inventoryApi = {
  list: (params) => http.get('/inventory', { params }),
  inbound: (data) => http.post('/inventory/inbound', data),
  outbound: (data) => http.post('/inventory/outbound', data),
  logs: (params) => http.get('/inventory/logs', { params })
}

export const authApi = {
  login: (data) => http.post('/auth/login', data),
  register: (data) => http.post('/auth/register', data),
  forgotPassword: (data) => http.post('/auth/forgot-password', data)
}

export const userApi = {
  profile: () => http.get('/users/profile'),
  list: () => http.get('/users'),
  updateStatus: (id, status) => http.put(`/users/${id}/status`, null, { params: { status } })
}

export const adminApi = {
  stats: () => http.get('/admin/stats')
}
