import { verifyCode } from '../../utils/api'

Component({
  data: {
    code: '',
    loading: false,
    result: null
  },
  
  methods: {
    onCodeInput(e) {
      this.setData({ code: e.detail.value })
    },
    
    async handleVerify() {
      const { code } = this.data
      if (!code.trim()) {
        wx.showToast({
          title: '请输入防伪码',
          icon: 'none'
        })
        return
      }
      
      this.setData({ loading: true })
      
      try {
        const response = await verifyCode(code.trim())
        
        if (response.code === 200) {
          this.setData({
            result: {
              ...response.data,
              valid: true,
              message: '验证成功！正品保证'
            }
          })
        } else {
          this.setData({
            result: {
              valid: false,
              message: response.message || '验证失败'
            }
          })
        }
      } catch (error) {
        console.error('验证失败:', error)
        this.setData({
          result: {
            valid: false,
            message: '验证失败，请稍后重试'
          }
        })
        wx.showToast({
          title: '验证失败',
          icon: 'error'
        })
      } finally {
        this.setData({ loading: false })
      }
    },
    
    formatDate(dateStr) {
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
  }
})