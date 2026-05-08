<template>
  <div class="auth-container admin-theme">
    <div class="auth-card">
      <div class="auth-header">
        <span class="auth-logo">OS</span>
        <h2>管理员登录</h2>
        <p>OmniStock 后台管理系统</p>
      </div>
      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label>管理账号</label>
          <input v-model="form.username" type="text" placeholder="请输入管理员用户名" required />
        </div>
        <div class="form-group">
          <label>管理密码</label>
          <input v-model="form.password" type="password" placeholder="请输入管理员密码" required />
        </div>
        <button type="submit" class="auth-btn admin-btn" :disabled="loading">
          {{ loading ? '验证中...' : '安全登录' }}
        </button>
      </form>
      <div class="auth-footer">
        <router-link to="/login">返回用户登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, inject } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@/api'

const router = useRouter()
const toast = inject('toast')
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  loading.value = true
  try {
    const data = await authApi.login(form)
    
    // 强制校验是否具有管理员角色
    if (!data.roles || !data.roles.includes('ROLE_ADMIN')) {
      toast.error('权限不足：仅允许管理员账号登录此通道')
      return
    }

    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data))
    toast.success('管理员验证成功')
    router.push('/users')
  } catch (error) {
    toast.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #16233a 0%, #0f172a 100%);
  position: relative;
  overflow: hidden;
}
.auth-container.admin-theme::before {
  background: rgba(255, 255, 255, 0.01);
}
.auth-card {
  position: relative;
  z-index: 1;
  width: 420px;
  padding: 48px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 20px 50px rgba(0,0,0,0.3);
}
.auth-header {
  text-align: center;
  margin-bottom: 32px;
}
.auth-logo {
  display: inline-block;
  width: 48px;
  height: 48px;
  line-height: 48px;
  background: #001529;
  color: #fff;
  border-radius: 8px;
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 16px;
}
.auth-header h2 {
  margin: 0;
  font-size: 24px;
  color: #001529;
}
.auth-header p {
  color: #8c8c8c;
  margin-top: 8px;
}
.auth-form .form-group {
  margin-bottom: 20px;
}
.auth-form label {
  display: block;
  margin-bottom: 8px;
  color: #595959;
}
.auth-form input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  transition: all 0.3s;
}
.auth-form input:focus {
  border-color: #001529;
  outline: none;
  box-shadow: 0 0 0 2px rgba(0,21,41,0.1);
}
.admin-btn {
  width: 100%;
  padding: 12px;
  background: #001529;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}
.admin-btn:hover {
  background: #002140;
}
.admin-btn:disabled {
  background: #bfbfbf;
  cursor: not-allowed;
}
.auth-footer {
  text-align: center;
  margin-top: 24px;
  color: #8c8c8c;
}
.auth-footer a {
  color: #1890ff;
  text-decoration: none;
}
</style>
