<script setup>
import { ref, onMounted } from 'vue'

const activeTab = ref('available') // 'available' or 'records'
const inventoryList = ref([])
const records = ref([])
const loading = ref(false)
const actionLoading = ref(null)

const getToken = () => localStorage.getItem('token')

const fetchInventory = async () => {
  loading.value = true
  try {
    const res = await fetch('http://localhost:8080/api/books/inventory', {
      headers: { 'Authorization': `Bearer ${getToken()}` }
    })
    inventoryList.value = await res.json()
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const fetchRecords = async () => {
  loading.value = true
  try {
    const res = await fetch('http://localhost:8080/api/books/records', {
      headers: { 'Authorization': `Bearer ${getToken()}` }
    })
    records.value = await res.json()
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const borrowBook = async (inventoryId) => {
  actionLoading.value = inventoryId
  try {
    const res = await fetch(`http://localhost:8080/api/books/inventory/${inventoryId}/borrow`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${getToken()}` }
    })
    if (!res.ok) throw new Error(await res.text())
    await fetchInventory()
    alert('Book borrowed successfully!')
  } catch (err) {
    alert(err.message || 'Failed to borrow book')
  } finally {
    actionLoading.value = null
  }
}

const returnBook = async (recordId) => {
  actionLoading.value = recordId
  try {
    const res = await fetch(`http://localhost:8080/api/books/records/${recordId}/return`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${getToken()}` }
    })
    if (!res.ok) throw new Error(await res.text())
    await fetchRecords()
    alert('Book returned successfully!')
  } catch (err) {
    alert(err.message || 'Failed to return book')
  } finally {
    actionLoading.value = null
  }
}

onMounted(() => {
  fetchInventory()
})

const switchTab = (tab) => {
  activeTab.value = tab
  if (tab === 'available') fetchInventory()
  else fetchRecords()
}

const availableBooks = () => {
  return inventoryList.value.filter(item => item.status === '在庫')
}

const unreturnedRecords = () => {
  return records.value.filter(record => !record.returnTime)
}
</script>

<template>
  <div class="library-wrapper">
    <div class="tabs">
      <button 
        :class="['tab-btn', { active: activeTab === 'available' }]" 
        @click="switchTab('available')"
      >
        Available Books
      </button>
      <button 
        :class="['tab-btn', { active: activeTab === 'records' }]" 
        @click="switchTab('records')"
      >
        My Borrowed Books
      </button>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="loader"></div>
      <p>Loading data...</p>
    </div>

    <!-- Available Books -->
    <div v-else-if="activeTab === 'available'" class="grid-container">
      <div v-if="availableBooks().length === 0" class="empty-state">
        <p>No books currently available to borrow.</p>
      </div>
      <div v-else v-for="item in availableBooks()" :key="item.inventoryId" class="glass-card book-card">
        <div class="book-info">
          <h3>{{ item.book.name }}</h3>
          <p class="author">by {{ item.book.author }}</p>
          <p class="desc">{{ item.book.introduction }}</p>
          <div class="meta">
            <span class="tag">ISBN: {{ item.book.isbn }}</span>
            <span class="tag status-tag">Inventory ID: {{ item.inventoryId }}</span>
          </div>
        </div>
        <button 
          class="btn-primary borrow-btn" 
          @click="borrowBook(item.inventoryId)"
          :disabled="actionLoading === item.inventoryId"
        >
          {{ actionLoading === item.inventoryId ? 'Borrowing...' : 'Borrow Book' }}
        </button>
      </div>
    </div>

    <!-- Borrowed Records -->
    <div v-else-if="activeTab === 'records'" class="grid-container">
      <div v-if="unreturnedRecords().length === 0" class="empty-state">
        <p>You have no books to return.</p>
      </div>
      <div v-else v-for="record in unreturnedRecords()" :key="record.id" class="glass-card book-card">
        <div class="book-info">
          <h3>{{ record.inventory.book.name }}</h3>
          <p class="author">by {{ record.inventory.book.author }}</p>
          <div class="meta">
            <span class="tag">Borrowed: {{ new Date(record.borrowingTime).toLocaleDateString() }}</span>
            <span class="tag">Inventory ID: {{ record.inventory.inventoryId }}</span>
          </div>
        </div>
        <button 
          class="btn-return" 
          @click="returnBook(record.id)"
          :disabled="actionLoading === record.id"
        >
          {{ actionLoading === record.id ? 'Returning...' : 'Return Book' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.library-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  animation: fadeIn 0.4s ease;
}

.tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 1rem;
}

.tab-btn {
  background: transparent;
  border: none;
  color: #94a3b8;
  font-size: 1.1rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  cursor: pointer;
  position: relative;
  transition: color 0.3s;
}

.tab-btn:hover {
  color: #e2e8f0;
}

.tab-btn.active {
  color: #fff;
}

.tab-btn.active::after {
  content: '';
  position: absolute;
  bottom: -1rem;
  left: 0;
  width: 100%;
  height: 3px;
  background: #6366f1;
  border-radius: 3px 3px 0 0;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

.book-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 1.5rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
}

.book-info h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: #fff;
}

.author {
  color: #818cf8;
  font-size: 0.95rem;
  margin-bottom: 1rem;
  font-weight: 500;
}

.desc {
  color: #cbd5e1;
  font-size: 0.9rem;
  line-height: 1.5;
  margin-bottom: 1.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.tag {
  background: rgba(255, 255, 255, 0.1);
  padding: 0.3rem 0.6rem;
  border-radius: 4px;
  font-size: 0.8rem;
  color: #94a3b8;
}

.status-tag {
  background: rgba(99, 102, 241, 0.2);
  color: #a5b4fc;
}

.borrow-btn {
  width: 100%;
}

.btn-return {
  width: 100%;
  padding: 0.85rem;
  border-radius: 8px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  background: linear-gradient(135deg, #f59e0b 0%, #ea580c 100%);
  color: white;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(234, 88, 12, 0.3);
}

.btn-return:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(234, 88, 12, 0.4);
}

.btn-return:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 4rem;
  color: #94a3b8;
  background: rgba(15, 23, 42, 0.4);
  border-radius: 12px;
  border: 1px dashed rgba(255, 255, 255, 0.1);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 5rem;
  color: #94a3b8;
}

.loader {
  border: 4px solid rgba(255,255,255,0.1);
  border-top: 4px solid #6366f1;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>
