<template>
  <div class="book-detail-container">
    <div class="book-header">
      <img :src="bookData.img" alt="Book Cover" class="book-cover" />
    </div>
    <div class="book-content">
      <iframe
        v-if="bookData.bookURL"
        :src="bookData.bookURL"
        class="book-reader"
        title="Book Reader">
      </iframe>
      <p v-else class="loading-message">Loading book content...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const bookData = ref({ bookURL: '', img: '' });
const username = localStorage.getItem('username') || '';

const fetchBookDetails = async () => {
  const bookId = route.params.bookId;
  try {
    console.log(username);
    console.log(bookId);
    const response = await axios.get('/api/bmr/user/v1/readBook', {
      params: {
        username,
        bookId,
      },
    });
    console.log(response);

    if (response.data && response.data.data) {
      bookData.value = response.data.data;
    }

  } catch (error) {
    console.error('Error fetching book details:', error);
  }
};

onMounted(() => {
  fetchBookDetails();
});
</script>

<style scoped>
.book-detail-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px auto;
  padding: 20px;
  max-width: 800px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background-color: #fff;
}

.book-header {
  width: 100%;
  text-align: center;
  margin-bottom: 20px;
}

.book-cover {
  width: 200px;
  height: auto;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.book-content {
  width: 100%;
  position: relative;
  padding: 10px;
}

.book-reader {
  width: 100%;
  height: 600px;
  border: none;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.loading-message {
  font-size: 18px;
  color: #555;
  text-align: center;
}
</style>
