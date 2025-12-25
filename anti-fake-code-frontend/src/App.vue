<template>
  <div id="app">
    <el-container>
      <el-header>
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
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
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
</style>