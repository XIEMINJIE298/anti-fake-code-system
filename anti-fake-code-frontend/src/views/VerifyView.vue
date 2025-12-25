<template>
  <div class="verify-view">
    <h1>防伪码验证系统</h1>
    <VerifyCode />
  </div>
</template>

<script setup>
import VerifyCode from '../components/VerifyCode.vue'
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