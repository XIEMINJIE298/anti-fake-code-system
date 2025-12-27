// src/api/index.js
import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 统一拦截器
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
}, error => Promise.reject(error))

request.interceptors.response.use(
  res => res.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      // 强制跳登录页
      window.location.href = '/login'
    }
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export default request