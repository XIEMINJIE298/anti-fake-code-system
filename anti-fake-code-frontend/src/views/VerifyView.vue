<template>
  <div class="verify-view">
    <h1>防伪码验证系统</h1>
    <VerifyCode />
  </div>
  <!-- 验证卡片下方 -->
  <el-card v-if="isAdmin" style="margin-top:20px;">
    <el-button type="primary" @click="$router.push('/admin')">
      进入操作台
    </el-button>
  </el-card>
</template>

<script setup>
import VerifyCode from '../components/VerifyCode.vue'
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isAdmin = ref(false)

/* 读取角色 */
function checkRole() {
  isAdmin.value = localStorage.getItem('role') === 'admin'
}

/* 首次进入 */
onMounted(checkRole)

/* 登录成功返回本页时，路由栈变化会触发 afterEach */
const removeHook = router.afterEach((to) => {
  if (to.name === 'Verify') checkRole()
})

/* 组件卸载时清理钩子 */
onBeforeUnmount(() => removeHook())
</script>

<style scoped>
.verify-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  color: #303133;
  margin-bottom: 30px;
}
</style>