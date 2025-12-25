<template>
  <div class="verify-container">
    <el-card class="verify-card" shadow="hover">
      <template #header>
        <h2>防伪码验证</h2>
      </template>
      
      <el-form :model="form" @submit.prevent="handleVerify" label-width="0">
        <el-form-item>
          <el-input
            v-model="form.code"
            placeholder="请输入16位防伪码"
            clearable
            size="large"
            maxlength="17"
          >
            <template #prepend>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleVerify" 
            :loading="loading"
            size="large"
            style="width: 100%"
          >
            {{ loading ? '验证中...' : '验证真伪' }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 验证结果 -->
    <el-card v-if="result" class="result-card" :class="{ 'success': result.valid, 'failed': !result.valid }">
      <template #header>
        <div class="result-header">
          <el-icon :size="24">
            <SuccessFilled v-if="result.valid" />
            <CircleCloseFilled v-else />
          </el-icon>
          <span>{{ result.message }}</span>
        </div>
      </template>
      
      <div v-if="result.valid" class="product-info">
        <h3>产品信息</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="产品序列号">{{ result.serialNumber }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ result.productName }}</el-descriptions-item>
          <el-descriptions-item label="产品类别">{{ result.category }}</el-descriptions-item>
          <el-descriptions-item label="产品型号">{{ result.model }}</el-descriptions-item>
          <el-descriptions-item label="生产批次">{{ result.batchNumber }}</el-descriptions-item>
          <el-descriptions-item label="生产厂家">{{ result.manufacturer }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="verify-info">
          <p>验证次数：<el-tag type="info">{{ result.verifyCount }} 次</el-tag></p>
          <p>首次验证：<el-tag type="info">{{ formatDate(result.firstVerifyTime) }}</el-tag></p>
          <p>本次验证：<el-tag type="info">{{ formatDate(result.currentVerifyTime) }}</el-tag></p>
        </div>
        
        <el-alert 
          v-if="result.verifyCount > 1"
          title="此防伪码已被多次验证，请确认是否为首次查询！"
          type="warning"
          show-icon
          :closable="false"
          class="warning-alert"
        />
      </div>
    </el-card>

    <!-- 验证历史 -->
    <el-card v-if="result && result.valid" class="history-card">
      <template #header>
        <h3>验证历史</h3>
      </template>
      <p class="history-tip">您可以通过企业管理后台查看详细的验证记录</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Lock, SuccessFilled, CircleCloseFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { verifyCode as verifyCodeApi } from '../api/antiFakeCode'
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'

const form = reactive({
  code: ''
})

const loading = ref(false)
const result = ref(null)

const route = useRoute()

onMounted(() => {
  const code = route.query.code
  if (code) {
    form.code = code.trim()
    handleVerify() 
  }
})

const handleVerify = async () => {
  if (!form.code.trim()) {
    ElMessage.warning('请输入防伪码')
    return
  }

  loading.value = true
  
  try {
    const response = await verifyCodeApi(form.code.trim())
    
    if (response.code === 200) {
      result.value = {
        ...response.data,
        valid: true
      }
    } else {
      result.value = {
        valid: false,
        message: response.message || '验证失败'
      }
    }
  } catch (error) {
    console.error('验证失败:', error)
    result.value = {
      valid: false,
      message: '验证失败，请稍后重试'
    }
    ElMessage.error('验证失败')
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '--'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}
</script>


<style scoped>
.verify-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.verify-card {
  margin-bottom: 30px;
}

.result-card.success {
  border-color: #67c23a;
}

.result-card.failed {
  border-color: #f56c6c;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: bold;
}

.product-info h3 {
  margin-bottom: 15px;
  color: #303133;
}

.verify-info {
  margin-top: 20px;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.verify-info p {
  margin: 0;
}

.warning-alert {
  margin-top: 15px;
}

.history-card {
  margin-top: 20px;
}

.history-tip {
  color: #909399;
  font-size: 14px;
}
</style>