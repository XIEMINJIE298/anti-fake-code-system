import { createRouter, createWebHistory } from 'vue-router'
import UploadView from '../views/UploadView.vue'
import VerifyView from '../views/VerifyView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/',
    redirect: '/login'
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
      title: '注册'   // 添加这个meta字段，隐藏导航栏
    }
  },
  {
    path: '/upload',
    name: 'Upload',
    component: UploadView,
    meta: {
      requiresRole: 'admin',
      title: '上传文件'
    }   // ← 只有 admin 能进
  },
  {
    path: '/verify',
    name: 'Verify',
    component: VerifyView,
    meta: {
      requiresRole: null,
      title: 'XMJ验码工具'
    }      // ← 所有人都能进
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/* ---------- 权限守卫 ---------- */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  /* 1. 动态设置页签标题 */
  document.title = to.meta.title || 'XMJ验码工具'   // 默认标题
  
  // 1. 未登录且目标不是登录/注册
  if (!token && !['/login', '/register'].includes(to.path)) {
    return next('/login')
  }

  // 2. 需要管理员但角色不符
  if (to.meta.requiresRole === 'admin' && role !== 'admin') {
    ElMessage.warning('权限不足')
    return next('/verify')
  }

  next()
})

export default router