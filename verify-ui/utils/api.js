const API_BASE = 'https://30b98cb6.r38.cpolar.top/api'

const request = (url, data = {}, method = 'POST') => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${API_BASE}${url}`,
      method,
      data,
      header: {
        'content-type': 'application/json'
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(res)
        }
      },
      fail: reject
    })
  })
}

// 验证防伪码
export const verifyCode = (code) => {
  return request('/verify', { code })
}