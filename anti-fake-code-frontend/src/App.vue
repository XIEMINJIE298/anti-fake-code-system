<template>
  <div id="app">
    <!-- 根据路由meta信息决定是否显示头部 -->
    <el-header v-if="!$route.meta.hideHeader">
      <el-menu 
        :default-active="$route.path" 
        mode="horizontal" 
        router
        class="menu"
      >
        <el-menu-item index="/upload">
          <el-icon><Upload /></el-icon>
          生成防伪码
        </el-menu-item>
        <el-menu-item index="/verify">
          <el-icon><Search /></el-icon>
          验证防伪码
        </el-menu-item>
      </el-menu>
    </el-header>
    
    <el-main :class="{ 'full-height': $route.meta.hideHeader }">
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import { Upload, Search } from '@element-plus/icons-vue'
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
</style>