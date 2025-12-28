<template>
  <div class="admin-layout">
    <div class="back-btn">
      <el-button type="info" :icon="ArrowLeft" circle @click="goBack" />
    </div>
    <el-tabs v-model="activeTab" @tab-click="goTab">
      <el-tab-pane label="操作台" name="/admin/upload" />
      <el-tab-pane label="数据统计" name="/admin/stats" />
    </el-tabs>

    <div class="admin-body">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route  = useRoute()
const router = useRouter()
const activeTab = ref(route.path)

watch(() => route.path, val => activeTab.value = val)

function goTab(pane) {
  router.push(pane.props.name)
}

const goBack = () => router.push('/verify')
</script>

<style scoped>
.admin-layout { padding: 16px; }
.admin-body   { margin-top: 16px; }
</style>