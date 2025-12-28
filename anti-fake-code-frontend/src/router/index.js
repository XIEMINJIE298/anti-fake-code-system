import { createRouter, createWebHistory } from 'vue-router'
import UploadView from '../views/UploadView.vue'
import VerifyView from '../views/VerifyView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/',
    redirect: '/verify' // 默认跳转到verify页面
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
    meta: {
      hideHeader: true, // 添加这个meta字段
      title: '登录'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView,
    meta: {
      hideHeader: true,
      title: '注册'
    }
  },
  // 把原来的 /upload 路由改成一个“布局”路由，真实页面放在 /admin/upload
  {
    path: '/admin',
    name: 'AdminLayout',
    component: () => import('@/views/AdminLayout.vue'), // 新建的布局壳
    redirect: '/admin/upload',
    meta: { requiresRole: 'admin' },
    children: [
      {
        path: 'upload',
        name: 'AdminUpload',
        component: () => import('@/views/UploadView.vue')
      },
      {
        path: 'stats',
        name: 'AdminStats',
        component: () => import('@/views/StatsPlaceholder.vue') // 先空壳
      }
    ]
  },
  {
    path: '/verify',
    name: 'Verify',
    component: VerifyView,
    meta: {
      requiresRole: null,
      title: 'XMJ验码工具'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/* ---------- 权限守卫 ---------- */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  /* 1. 动态设置页签标题 */
  document.title = to.meta.title || 'XMJ验码工具'

  next()
})

export default router