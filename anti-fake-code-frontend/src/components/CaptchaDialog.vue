<template>
  <el-dialog
    title="请输入计算结果"
    v-model="visible"
    width="320px"
    :close-on-click-modal="false"
    @closed="emit('fail')"
  >
    <div class="captcha-box">
      <img :src="imgUrl" @click="refresh" alt="captcha" />
      <el-input
        v-model="input"
        placeholder="计算结果"
        size="large"
        @keyup.enter="ok"
      />
    </div>
    <template #footer>
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" :loading="loading" @click="ok">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { checkCaptcha } from '@/api/antiFakeCode'
import { getCaptcha } from '@/api/antiFakeCode'

const props = defineProps({ modelValue: Boolean })
const emit = defineEmits(['update:modelValue', 'pass', 'fail'])

const visible = computed(() => props.modelValue)
const uuid  = ref('')
const input = ref('')
const loading = ref(false)

// 图片地址
const imgUrl = computed(() => getCaptcha({ uuid: uuid.value }));

function refresh() {
  // 兼容低版本
  const prefix = (window.crypto && window.crypto.randomUUID)
    ? window.crypto.randomUUID()
    : `${Date.now()}-${Math.random().toString(36).slice(2)}`;
  uuid.value = prefix;
  input.value = '';
}

async function ok() {
  if (!input.value) return ElMessage.warning('请输入结果')
  loading.value = true
  try {
    const res = await checkCaptcha({ uuid: uuid.value, code: input.value })
    if (res.code === 200) {
      emit('update:modelValue', false)
      emit('pass')          // 父组件接到通知再调 verify
    } else {
      ElMessage.error(res.message || '验证失败')
      loading.value = false;   // ← 这里
      refresh()
    }
  } finally {
    loading.value = false
  }
}
function cancel() {
  emit('update:modelValue', false)
  emit('fail')
}

// 首次加载
nextTick(refresh)
</script>

<style scoped>
.captcha-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}
.captcha-box img {
  cursor: pointer;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}
</style>