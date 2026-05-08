<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <span class="auth-logo">OS</span>
        <h2>加入 OmniStock</h2>
        <p>创建您的账号，开启高效仓储管理</p>
      </div>
      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="设置您的登录用户名" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="设置您的登录密码" required />
        </div>
        <div class="form-group">
          <label>邮箱 (必填)</label>
          <input v-model="form.email" type="email" placeholder="找回密码的唯一凭证" required />
        </div>
        <button type="submit" class="auth-btn" :disabled="loading">
          {{ loading ? '注册中...' : '立即注册' }}
        </button>
      </form>
      <div class="auth-footer">
        已有账号？ <router-link to="/login">立即登录</router-link>
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
  password: '',
  email: ''
})

const handleRegister = async () => {
  loading.value = true
  try {
    await authApi.register(form)
    toast.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    toast.error(error.message || '注册失败')
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
  background: linear-gradient(135deg, #16233a 0%, #2f54eb 100%);
  position: relative;
  overflow: hidden;
}
.auth-container::before {
  content: "";
  position: absolute;
  width: 1000px;
  height: 1000px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 50%;
  top: -200px;
  right: -200px;
}
.auth-card {
  position: relative;
  z-index: 1;
  width: 420px;
  padding: 48px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 20px 50px rgba(0,0,0,0.2);
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
  background: #2f54eb;
  color: #fff;
  border-radius: 8px;
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 16px;
}
.auth-header h2 {
  margin: 0;
  font-size: 24px;
  color: #1f1f1f;
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
  border-color: #2f54eb;
  outline: none;
  box-shadow: 0 0 0 2px rgba(47,84,235,0.1);
}
.auth-btn {
  width: 100%;
  padding: 12px;
  background: #2f54eb;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}
.auth-btn:hover {
  background: #1d39c4;
}
.auth-btn:disabled {
  background: #bfbfbf;
  cursor: not-allowed;
}
.auth-footer {
  text-align: center;
  margin-top: 24px;
  color: #8c8c8c;
}
.auth-footer a {
  color: #2f54eb;
  text-decoration: none;
}
</style>
