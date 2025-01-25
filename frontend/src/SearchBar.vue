<!-- src/SearchBar.vue -->
<template>
  <div class="search-bar">
    <div class="search-field">
      <label>书名：</label>
      <input type="text" v-model="title" @keyup.enter="emitSearch" />
    </div>
    <div class="search-field">
      <label>作者：</label>
      <input type="text" v-model="author" @keyup.enter="emitSearch" />
    </div>
    <div class="search-field">
      <label>分类：</label>
      <input type="text" v-model="category" @keyup.enter="emitSearch" />
    </div>
    <div class="search-field">
      <label>语言：</label>
      <input type="text" v-model="language" @keyup.enter="emitSearch" />
    </div>
    <button @click="emitSearch">搜索</button>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'

const emits = defineEmits(['search'])

const title = ref('')
const author = ref('')
const category = ref('')
const language = ref('')

function emitSearch() {
  emits('search', {
    title: title.value,
    author: author.value,
    category: category.value,
    language: language.value
  })
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  flex-wrap: wrap;
  margin: 16px 0;
}

.search-field {
  display: flex;
  flex-direction: column;
}
</style>
