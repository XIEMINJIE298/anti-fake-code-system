import request from './index'

import { computed } from 'vue'

// 上传Excel生成防伪码
export const uploadExcel = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post('/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    responseType: 'blob'
  })
}

// 验证防伪码
export const verifyCode = (code) => {
  return request.post('/verify', { code })
}

// 校验图形验证码
export const checkCaptcha = (data) => {
  return request.post('/captcha/check', data)
}


// 获取图形验证码
// antiFakeCode.js
// antiFakeCode.js
export const getCaptcha = (data) => {
  const uuid = data.uuid || 'default-uuid';
  return `/api/captcha/image?uuid=${uuid}`;   // 加 /api
};