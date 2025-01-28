<template>
  <NavBar />
  <div class="book-detail-container">
    <div class="book-header">
      <img :src="bookData.img" alt="Book Cover" class="book-cover" />
    </div>
    <div class="book-content">
      <div class="search-bar">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search for text..."
          class="search-input"
        />
        <button @click="searchText" class="search-button">Search</button>
        <button @click="prevMatch" class="prev-button" :disabled="matches.length === 0">
          Previous
        </button>
        <button @click="nextMatch" class="next-button" :disabled="matches.length === 0">
          Next
        </button>
      </div>
      <iframe
        ref="iframeRef"
        v-if="bookData.bookURL"
        :src="bookData.bookURL"
        class="book-reader"
        title="Book Reader">
      </iframe>
      <p v-else class="loading-message">Loading book content...</p>
    </div>

    <details v-if="matches.length > 0" class="search-results" open>
      <summary>共匹配到 {{ matches.length }} 处</summary>
      <div class="result-item">
        <p>
          <strong>第 {{ currentMatch.lineNumber }} 行:</strong>
          <span>{{ currentMatch.content }}</span>
        </p>
        <p> ({{ currentIndex + 1 }} / {{ matches.length }}) </p>
      </div>
    </details>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import NavBar from '../NavBar.vue';

const route = useRoute();
const bookData = ref({ bookURL: '', img: '' });
const username = localStorage.getItem('username') || '';

const searchQuery = ref('');
const matches = ref([]);
const currentIndex = ref(-1);
const iframeRef = ref(null);

const currentMatch = computed(() => {
  if (currentIndex.value >= 0 && currentIndex.value < matches.value.length) {
    return matches.value[currentIndex.value];
  }
  return { lineNumber: 0, content: '' };
});

const fetchBookDetails = async () => {
  const bookId = route.params.bookId;
  try {
    const response = await axios.get('/api/bmr/user/v1/readBook', {
      params: {
        username,
        bookId,
      },
    });

    if (response.data && response.data.data) {
      bookData.value = response.data.data;
    }
  } catch (error) {
    console.error('Error fetching book details:', error);
  }
};

const searchText = async () => {
  if (!searchQuery.value.trim() || !bookData.value.bookURL) return;

  try {
    const response = await axios.get('/api/bmr/project/v1/bookmark/textInternalsearchBykmp', {
      params: {
        URL: bookData.value.bookURL,
        word: searchQuery.value.trim(),
      },
    });

    if (response.data && response.data.data) {
      matches.value = response.data.data.map((line) => {
        const [lineNumber, content] = line.split(': ');
        return { lineNumber: parseInt(lineNumber, 10), content };
      });
      currentIndex.value = matches.value.length > 0 ? 0 : -1;
      highlightMatch();
    }
  } catch (error) {
    console.error('Error searching text:', error);
  }
};

const highlightMatch = () => {
  if (
    currentIndex.value >= 0 &&
    currentIndex.value < matches.value.length
  ) {
    const iframeDocument = iframeRef.value?.contentDocument || null;

    if (iframeDocument) {
      const match = matches.value[currentIndex.value];
      const paragraphs = iframeDocument.body.innerText.split('\n');
      iframeDocument.body.innerHTML = paragraphs
        .map((line, index) =>
          index + 1 === match.lineNumber
            ? `<mark style="background-color: yellow;">${line}</mark>`
            : line
        )
        .join('<br>');

      // Scroll to the matched line
      const matchElement = iframeDocument.querySelector('mark');
      if (matchElement) {
        matchElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
    }
  }
};

const prevMatch = () => {
  if (matches.value.length > 0) {
    currentIndex.value =
      (currentIndex.value - 1 + matches.value.length) % matches.value.length;
    highlightMatch();
  }
};

const nextMatch = () => {
  if (matches.value.length > 0) {
    currentIndex.value = (currentIndex.value + 1) % matches.value.length;
    highlightMatch();
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

.search-bar {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.search-button,
.prev-button,
.next-button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
}

.search-button:hover,
.prev-button:hover,
.next-button:hover {
  background-color: #0056b3;
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

.search-results {
  width: 100%;
  margin-top: 10px;
  border: 1px solid #eee;
  border-radius: 5px;
  padding: 10px;
}

.search-results > summary {
  font-size: 18px;
  cursor: pointer;
}

.result-item {
  margin-top: 10px;
}
</style>
