import { createRouter, createWebHistory } from 'vue-router'
import UploadView from '../views/UploadView.vue'
import VerifyView from '../views/VerifyView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  { path: '/login', 
    name: 'Login', 
    component: LoginView,
    meta: {
      hideHeader: true // 添加这个meta字段
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView,
    meta: {
      hideHeader: true  // 添加这个meta字段，隐藏导航栏
    }
  },
  {
    path: '/upload',
    name: 'Upload',
    component: UploadView
  },
  {
    path: '/verify',
    name: 'Verify',
    component: VerifyView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router