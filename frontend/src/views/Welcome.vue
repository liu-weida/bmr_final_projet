<template>
  <div>
    <!-- 导航栏 -->
    <NavBar />

    <main>
      <h1>Welcome Page</h1>
      <p>这是一个欢迎界面</p>
    </main>
  </div>
</template>

<script setup>
import NavBar from '../NavBar.vue';
</script>

<style scoped>
main {
  padding: 1rem;
}
</style>
