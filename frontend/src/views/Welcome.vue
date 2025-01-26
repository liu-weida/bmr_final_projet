<template>
  <div>
    <!-- 导航栏 -->
    <NavBar />

    <main>
      <h1>Welcome Page</h1>
      <p>这是一个欢迎界面</p>

      <!-- 搜索栏 -->
      <SearchBar @search="handleSearch" />

      <!-- 书籍展示 -->
      <section class="book-list">
        <h2>推荐书籍</h2>
        <div v-if="books.length === 0" class="no-books">暂无书籍</div>
        <div v-else class="books-grid">
          <div
            v-for="book in books"
            :key="book.id"
            class="book-card"
          >
            <h3>{{ book.title }}</h3>
            <p>作者: {{ book.author }}</p>
            <p>分类: {{ book.category }}</p>
            <p class="description">{{ truncateDescription(book.description) }}</p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import NavBar from '../NavBar.vue';
import SearchBar from '../SearchBar.vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const books = ref([]); // 存储书籍列表
const pageSize = 20; // 每页显示的书籍数量

// 截取描述字符串
function truncateDescription(description, length = 100) {
  if (!description) return '';
  return description.length > length ? `${description.slice(0, length)}...` : description;
}

// 获取书籍数据
async function fetchBooks(params = {}) {
  try {
    const response = await fetch('/api/bmr/user/v1/bookSearch_page', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      params: JSON.stringify({
        pageNo: 1,
        pageSize: 20,
      }),
    });

    const data = await response.json();
    if (data.code === '0') {
      books.value = data.data.records || [];
    } else {
      console.error('获取书籍失败:', data.message);
    }
  } catch (error) {
    console.error('获取书籍异常:', error);
  }
}

// 处理搜索
function handleSearch(payload) {
  router.push({
    name: 'Search',
    params: { pageNo: 1 },
    query: payload,
  });
}

// 初始化数据
onMounted(() => {
  fetchBooks();
});
</script>

<style scoped>
main {
  padding: 1rem;
}

.book-list {
  margin-top: 2rem;
}

.book-list h2 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #333;
}

.no-books {
  text-align: center;
  color: #666;
  font-size: 1rem;
  margin-top: 1rem;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 每行 4 本书 */
  gap: 1rem;
}

.book-card {
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 1rem;
  text-align: left;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.book-card h3 {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.book-card p {
  font-size: 0.9rem;
  color: #555;
  margin: 0.3rem 0;
}

.book-card .description {
  color: #777;
  font-size: 0.85rem;
  line-height: 1.4;
}
</style>
