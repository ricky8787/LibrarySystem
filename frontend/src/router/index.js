import { createRouter, createWebHistory } from 'vue-router'
import Auth from '../components/Auth.vue'
import Library from '../components/Library.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    redirect: '/library'
  },
  {
    path: '/auth',
    name: 'Auth',
    component: Auth,
    meta: { requiresGuest: true }
  },
  {
    path: '/library',
    name: 'Library',
    component: Library,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    next('/auth')
  } else if (to.meta.requiresGuest && token) {
    next('/library')
  } else {
    next()
  }
})

export default router
