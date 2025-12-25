<template>
  <div class="upload-container">
    <el-upload
      class="upload-demo"
      drag
      action="#"
      :auto-upload="false"
      :on-change="handleFileChange"
      :limit="1"
      accept=".xlsx,.xls"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        拖拽文件到此处或 <em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          仅支持 .xlsx 格式文件，文件大小不超过 10MB
        </div>
      </template>
    </el-upload>

    <div class="button-group">
      <el-button 
        type="primary" 
        @click="uploadFile"
        :loading="loading"
        :disabled="!selectedFile"
      >
        {{ loading ? '生成中...' : '生成防伪码' }}
      </el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <el-alert
      v-if="result"
      :title="result.message"
      :type="result.type"
      show-icon
      closable
      class="result-alert"
    />

    <el-progress
      v-if="loading"
      :percentage="progress"
      :indeterminate="true"
      class="progress-bar"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { uploadExcel } from '../api/antiFakeCode'
import fileDownload from 'js-file-download'

const selectedFile = ref(null)
const loading = ref(false)
const progress = ref(0)
const result = ref(null)

const handleFileChange = (file) => {
  selectedFile.value = file.raw
  result.value = null
}

const uploadFile = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择文件')
    return
  }

  loading.value = true
  progress.value = 20

  try {
    const response = await uploadExcel(selectedFile.value)
    
    progress.value = 80
    
    // 创建下载文件名
    const fileName = `防伪码_${selectedFile.value.name}`
    fileDownload(response, fileName)
    
    result.value = {
      message: `成功生成并下载防伪码文件！`,
      type: 'success'
    }
    
    ElMessage.success('生成成功！')

  } catch (error) {
    console.error('上传失败:', error)
    result.value = {
      message: `生成失败: ${error.message || '未知错误'}`,
      type: 'error'
    }
    ElMessage.error('生成失败')
  } finally {
    loading.value = false
    progress.value = 100
    setTimeout(() => {
      progress.value = 0
    }, 500)
  }
}

const reset = () => {
  selectedFile.value = null
  result.value = null
  loading.value = false
  progress.value = 0
}
</script>

<style scoped>
.upload-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 40px 20px;
}

.button-group {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 10px;
}

.result-alert {
  margin-top: 20px;
}

.progress-bar {
  margin-top: 20px;
}

:deep(.el-upload-dragger) {
  padding: 40px;
}
</style>