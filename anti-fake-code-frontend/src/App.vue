<template>
  <div id="app">
    <!-- 根据路由meta信息决定是否显示头部 -->
    <el-header v-if="!$route.meta.hideHeader">
      <!-- App.vue 的 el-header 内部，el-menu 结束位置 -->
      <el-menu mode="horizontal" router class="menu" :ellipsis="false">
        <el-menu-item index="/upload" v-if="role === 'admin'">
          <el-icon>
            <Upload />
          </el-icon>
          生成防伪码
        </el-menu-item>
        <el-menu-item index="/verify">
          <el-icon>
            <Search />
          </el-icon>
          验证防伪码
        </el-menu-item>

        <!-- 右侧退出 -->
        <div class="flex-grow" />
        <el-sub-menu index="user">
          <template #title>
            <el-icon>
              <Avatar />
            </el-icon>
            {{ username }}
          </template>
          <el-menu-item index="logout" @click="handleLogout">
            退出登录
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>

    <el-main :class="{ 'full-height': $route.meta.hideHeader }">
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import { useAuth } from '@/composables/useAuth'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { computed } from 'vue'
import { Upload, Search, Avatar } from '@element-plus/icons-vue'

const router = useRouter()

/* 读本地数据 */
const token  = localStorage.getItem('token')
const role = computed(() => localStorage.getItem('role'))
const username = computed(() => localStorage.getItem('username') || '用户')

/* 退出函数 */
const handleLogout = () => {
  localStorage.clear()
  ElMessage.success('已退出登录')
  router.replace('/login')      // 跳登录页（不留历史）
}
const { isAdmin } = useAuth()
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

.menu {
  border-bottom: none;
  max-width: 1200px;
  margin: 0 auto;
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
/* App.vue <style> 里加一行 */
.flex-grow { flex: 1; }
</style>