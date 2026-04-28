<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const token = ref(localStorage.getItem('token') || '')
const userName = ref(localStorage.getItem('userName') || '')

// Listen to route changes to update header dynamically
watch(route, () => {
  token.value = localStorage.getItem('token') || ''
  userName.value = localStorage.getItem('userName') || ''
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userName')
  token.value = ''
  userName.value = ''
  router.push('/auth')
}
</script>

<template>
  <div class="app-container">
    <nav class="navbar" v-if="token">
      <div class="nav-brand">Library<span>Pro</span></div>
      <div class="nav-user">
        <span class="user-greeting">Welcome, {{ userName }}</span>
        <button class="btn-logout" @click="handleLogout">Logout</button>
      </div>
    </nav>
    
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 3rem;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-brand {
  font-size: 1.5rem;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
}

.nav-brand span {
  color: #6366f1;
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.user-greeting {
  color: #e2e8f0;
  font-size: 0.95rem;
  font-weight: 500;
}

.btn-logout {
  background: transparent;
  color: #ef4444;
  border: 1px solid rgba(239, 68, 68, 0.3);
  padding: 0.5rem 1.2rem;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-logout:hover {
  background: rgba(239, 68, 68, 0.1);
  border-color: #ef4444;
  transform: translateY(-1px);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 2rem;
}
</style>
