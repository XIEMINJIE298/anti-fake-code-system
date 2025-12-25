import { createRouter, createWebHistory } from 'vue-router'
import UploadView from '../views/UploadView.vue'
import VerifyView from '../views/VerifyView.vue'

const routes = [
  {
    path: '/',
    redirect: '/upload'
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