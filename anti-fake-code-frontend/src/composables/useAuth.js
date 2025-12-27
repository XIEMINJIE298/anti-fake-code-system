// src/composables/useAuth.js
import { ref, computed } from 'vue'
export const useAuth = () => {
  const token = ref(localStorage.getItem('token'))
  const role = ref(localStorage.getItem('role'))

  const isAdmin = computed(() => role.value === 'admin')
  const isUser  = computed(() => role.value === 'user')

  const logout = () => {
    localStorage.clear()
    token.value = ''
    role.value = ''
  }

  return { token, role, isAdmin, isUser, logout }
}