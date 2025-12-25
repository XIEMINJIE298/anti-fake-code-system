import request from './index'

// 上传Excel生成防伪码
export const uploadExcel = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post('/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    responseType: 'blob' // 重要：用于文件下载
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