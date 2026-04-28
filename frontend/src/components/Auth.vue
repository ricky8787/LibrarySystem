<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const isLogin = ref(true)
const form = ref({
  phone: '',
  password: '',
  name: ''
})
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

const toggleMode = () => {
  isLogin.value = !isLogin.value
  errorMsg.value = ''
  successMsg.value = ''
  form.value = { phone: '', password: '', name: '' }
}

const handleSubmit = async () => {
  errorMsg.value = ''
  successMsg.value = ''
  loading.value = true
  
  const endpoint = isLogin.value ? '/api/auth/login' : '/api/auth/register'
  
  try {
    const response = await fetch(`http://localhost:8080${endpoint}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
    
    const text = await response.text()
    let data
    try {
      data = JSON.parse(text)
    } catch {
      data = text
    }

    if (!response.ok) {
      throw new Error(data || 'An error occurred')
    }
    
    if (isLogin.value) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('userName', data.phone)
      router.push('/library')
    } else {
      successMsg.value = 'Registration successful! Please login.'
      setTimeout(() => {
        isLogin.value = true
        form.value.password = ''
      }, 1500)
    }
  } catch (err) {
    errorMsg.value = err.message
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-wrapper">
    <div class="glass-card auth-card">
      <div class="card-header">
        <h2>{{ isLogin ? 'Welcome Back' : 'Create Account' }}</h2>
        <p>{{ isLogin ? 'Login to access the library' : 'Join our premium library system' }}</p>
      </div>
      
      <div v-if="errorMsg" class="alert error-alert">{{ errorMsg }}</div>
      <div v-if="successMsg" class="alert success-alert">{{ successMsg }}</div>

      <form @submit.prevent="handleSubmit" class="auth-form">
        <div class="input-group" v-if="!isLogin">
          <label>Full Name</label>
          <input type="text" v-model="form.name" required placeholder="John Doe" />
        </div>
        
        <div class="input-group">
          <label>Phone Number</label>
          <input type="tel" v-model="form.phone" required placeholder="e.g. 0912345678" />
        </div>
        
        <div class="input-group">
          <label>Password</label>
          <input type="password" v-model="form.password" required placeholder="••••••••" />
        </div>

        <button type="submit" class="btn-primary w-full" :disabled="loading">
          <span v-if="loading" class="spinner"></span>
          {{ isLogin ? 'Sign In' : 'Sign Up' }}
        </button>
      </form>

      <div class="auth-footer">
        <p>
          {{ isLogin ? "Don't have an account?" : "Already have an account?" }}
          <a href="#" @click.prevent="toggleMode" class="text-link">
            {{ isLogin ? 'Register now' : 'Login here' }}
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 2.5rem;
  animation: slideUp 0.6s cubic-bezier(0.16, 1, 0.3, 1);
}

.card-header {
  text-align: center;
  margin-bottom: 2rem;
}

.card-header h2 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #fff 0%, #a5b4fc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.card-header p {
  color: #94a3b8;
  font-size: 0.95rem;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-group label {
  font-size: 0.85rem;
  color: #cbd5e1;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.input-group input {
  padding: 0.85rem 1rem;
  border-radius: 8px;
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.input-group input:focus {
  outline: none;
  border-color: #6366f1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
  background: rgba(15, 23, 42, 0.8);
}

.btn-primary {
  margin-top: 1rem;
  padding: 0.9rem;
  font-size: 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
}

.auth-footer {
  margin-top: 2rem;
  text-align: center;
  font-size: 0.9rem;
  color: #94a3b8;
}

.text-link {
  color: #818cf8;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}

.text-link:hover {
  color: #a5b4fc;
}

.alert {
  padding: 0.75rem 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
  animation: fadeIn 0.3s ease;
}

.error-alert {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #fca5a5;
}

.success-alert {
  background: rgba(34, 197, 94, 0.1);
  border: 1px solid rgba(34, 197, 94, 0.3);
  color: #86efac;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
