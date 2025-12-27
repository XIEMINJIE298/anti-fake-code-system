<template>
  <div class="login-container">
    <div class="login-form">
      <h2>用户登录</h2>
      <el-form :model="form" @submit.prevent="doLogin">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item>
          <el-button native-type="submit" type="primary" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 新增注册链接 -->
      <div class="register-link">
        <el-button type="text" @click="goToRegister">
          没有账号？立即注册
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = ref({ username: '', password: '' })

const doLogin = async () => {
  try {
    const res = await login(form.value)
    // 能走到这里说明 http 状态是 2xx
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('role', res.data.role)
      router.push('/verify')
    } else {
      // 后端提示账号密码错，但 http 仍是 200
      ElMessage.error(res.message || '账号密码错误')
    }
  } catch (error) {
    const msg = error.response?.data?.message || error.message || '网络异常'
    ElMessage.error(msg)
  }
}

// 新增：跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.login-form {
  width: 400px;
  padding: 20px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}
</style>