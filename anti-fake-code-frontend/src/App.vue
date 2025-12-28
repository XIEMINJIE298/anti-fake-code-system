<template>
  <div id="app">
    <el-header v-if="!$route.meta.hideHeader">
      <el-menu mode="horizontal" router class="menu" :ellipsis="false">
        <!-- 未登录：左上角显示「管理员登录」 -->
        <el-menu-item v-if="!token" index="/login" @click="$router.push('/login')">
          <el-icon><Avatar /></el-icon>
          管理员登录
        </el-menu-item>

        <div class="flex-grow" />

        <!-- 已登录：右上角显示用户名 + 退出 -->
        <el-sub-menu v-if="token" index="user">
          <template #title>
            <el-icon><Avatar /></el-icon>
            {{ username }}
          </template>
          <el-menu-item index="logout" @click="handleLogout">退出登录</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>

    <el-main :class="{ 'full-height': $route.meta.hideHeader }">
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Avatar } from '@element-plus/icons-vue'

const router = useRouter()

/* 响应式状态 */
const token   = ref('')
const role    = ref('')
const username = ref('用户')

/* 读 localStorage 并刷新状态 */
function refreshAuth() {
  token.value   = localStorage.getItem('token') || ''
  role.value    = localStorage.getItem('role') || ''
  username.value = localStorage.getItem('username') || '用户'
}

/* 退出 */
function handleLogout() {
  localStorage.clear()
  refreshAuth()
  ElMessage.success('已退出登录')
  router.replace('/verify')
}

/* 首次加载 + 路由变化时刷新 */
onMounted(refreshAuth)
router.afterEach(refreshAuth)
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.el-header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.login-button {
  margin-left: 20px;
}

.menu {
  border-bottom: none;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-main {
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 登录页面全屏样式 */
.el-main.full-height {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-grow {
  flex: 1;
}
</style>