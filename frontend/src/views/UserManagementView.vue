<template>
  <div class="user-management">
    <div class="page-header">
      <h2>用户管理</h2>
      <p>管理系统用户信息及账号状态</p>
    </div>

    <div class="table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email || '-' }}</td>
            <td>
              <span v-for="role in user.roles" :key="role" class="role-tag">
                {{ role === 'ROLE_ADMIN' ? '管理员' : '普通用户' }}
              </span>
            </td>
            <td>
              <span :class="['status-dot', user.status === 1 ? 'active' : 'inactive']"></span>
              {{ user.status === 1 ? '正常' : '禁用' }}
            </td>
            <td>{{ formatDate(user.createdTime) }}</td>
            <td>
              <button 
                class="action-btn" 
                @click="toggleStatus(user)"
                :disabled="user.username === 'admin'"
              >
                {{ user.status === 1 ? '禁用' : '启用' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { userApi } from '@/api'

const toast = inject('toast')
const users = ref([])

const fetchUsers = async () => {
  try {
    users.value = await userApi.list()
  } catch (error) {
    toast.error('获取用户列表失败')
  }
}

const toggleStatus = async (user) => {
  const newStatus = user.status === 1 ? 0 : 1
  try {
    await userApi.updateStatus(user.id, newStatus)
    toast.success(`${newStatus === 1 ? '启用' : '禁用'}成功`)
    fetchUsers()
  } catch (error) {
    toast.error('操作失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString()
}

onMounted(fetchUsers)
</script>

<style scoped>
.user-management {
  padding: 24px;
}
.page-header {
  margin-bottom: 24px;
}
.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #1f1f1f;
}
.page-header p {
  margin: 8px 0 0;
  color: #8c8c8c;
}
.table-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  overflow: hidden;
}
.data-table {
  width: 100%;
  border-collapse: collapse;
}
.data-table th, .data-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}
.data-table th {
  background: #fafafa;
  font-weight: 600;
  color: #262626;
}
.role-tag {
  display: inline-block;
  padding: 2px 8px;
  background: #e6f7ff;
  border: 1px solid #91d5ff;
  color: #1890ff;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 4px;
}
.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
}
.status-dot.active { background: #52c41a; }
.status-dot.inactive { background: #ff4d4f; }

.action-btn {
  padding: 4px 12px;
  border: 1px solid #d9d9d9;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}
.action-btn:hover:not(:disabled) {
  border-color: #2f54eb;
  color: #2f54eb;
}
.action-btn:disabled {
  color: #bfbfbf;
  cursor: not-allowed;
}
</style>
