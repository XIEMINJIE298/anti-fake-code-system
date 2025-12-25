<template>
  <div class="upload-view">
    <h1>防伪码生成系统</h1>
    <UploadExcel />
  </div>
</template>

<script setup>
import UploadExcel from '../components/UploadExcel.vue'
</script>

<style scoped>
.upload-view {
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