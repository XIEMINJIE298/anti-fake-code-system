<template>
  <div class="register-container">
    <div class="register-form">
      <h2>用户注册</h2>
      <el-form :model="form" @submit.prevent="handleRegister">
        <el-form-item>
          <el-input 
            v-model="form.username" 
            placeholder="用户名" 
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="密码" 
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            native-type="submit" 
            size="large" 
            :loading="loading"
            style="width: 100%"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-link">
        <el-button type="text" @click="goToLogin">
          已有账号？立即登录
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'

const router = useRouter()
const loading = ref(false)
const form = reactive({ 
  username: '', 
  password: '' 
})

const handleRegister = async () => {
  if (!form.username.trim() || !form.password.trim()) {
    ElMessage.error('请输入用户名和密码')
    return
  }

  loading.value = true
  
  try {
    const res = await register(form)
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } else {
      ElMessage.error(res.message || '注册失败')
    }
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.register-form {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.register-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}
</style>