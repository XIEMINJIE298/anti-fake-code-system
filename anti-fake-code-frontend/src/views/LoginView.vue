<template>
  <div class="login-container">
    <!-- 左上角返回按钮 -->
    <div class="back-btn">
      <el-button type="info" :icon="ArrowLeft" circle @click="goBack" />
    </div>

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
import { ArrowLeft } from '@element-plus/icons-vue'   // ① 引入图标

const router = useRouter()
const form = ref({ username: '', password: '' })

const doLogin = async () => {
  try {
    const res = await login(form.value)
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('role', res.data.role)
      // LoginView.vue 里
      if (res.code === 200) {
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('role', res.data.role)
        router.replace('/verify')
      }
    } else {
      ElMessage.error(res.message || '账号密码错误')
    }
  } catch (error) {
    const msg = error.response?.data?.message || error.message || '网络异常'
    ElMessage.error(msg)
  }
}

const goToRegister = () => router.push('/register')

// ② 返回首页
const goBack = () => router.push('/verify')
</script>

<style scoped>
.login-container {
  position: relative;
  /* 让内部绝对定位参考这里 */
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.back-btn {
  position: absolute;
  top: 24px;
  left: 24px;
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