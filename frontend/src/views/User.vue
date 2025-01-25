<!-- src/views/User.vue -->
<template>
  <div class="user-container">
    <header class="user-header">
      <div class="header-left">
        <span class="username">{{ username }}</span>
      </div>
      <div class="header-right">
        <button @click="logout">注销</button>
      </div>
    </header>

    <div class="search-wrapper">
      <!-- 放置我们的4输入框 SearchBar -->
      <SearchBar @search="handleSearch" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import SearchBar from '../SearchBar.vue'

const route = useRoute()
const router = useRouter()

// 当前用户名
const username = ref(route.params.username || '')

// 注销操作
async function logout() {
  const token = localStorage.getItem('token') || ''
  try {
    const response = await axios.delete('/api/bmr/user/v1/user/logout', {
      params: {
        username: username.value,
        token
      }
    })
    if (response.data.code === '200') {
      // 注销成功
      localStorage.removeItem('token')
      router.push({ name: 'login' })
    } else {
      console.error('注销失败:', response.data.message)
    }
  } catch (error) {
    console.error('注销异常:', error)
  }
}

// 当用户在搜索栏点击“搜索”时的回调
function handleSearch(payload) {
  // payload 里包含 { title, author, category, language }
  // 这里假设点击搜索后，跳转到搜索页面的第1页
  router.push({
    name: 'Search',            // 在 router/index.js 里配置的 route 名
    params: { pageNo: 1 },     // path: /search/1
    query: {
      title: payload.title,
      author: payload.author,
      category: payload.category,
      language: payload.language
    }
  })
}
</script>

<style scoped>
.user-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #f1f1f1;
}

.header-left .username {
  font-weight: bold;
  margin-right: 16px;
}

.search-wrapper {
  margin-top: 32px;
  display: flex;
  justify-content: center;
}
</style>
