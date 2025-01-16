<template>
  <div>
    <h1>Welcome Page</h1>
    <button @click="goLogin">去登录</button>
    <button @click="goRegister">去注册</button>
  </div>
</template>

<script>
export default {
  name: 'Welcome',
  setup() {
    const goLogin = () => {
      // 使用vue-router跳转
      window.location.href = '/login'; // 或者用编程式导航：this.$router.push('/login')
    };
    const goRegister = () => {
      window.location.href = '/register';
    };

    return { goLogin, goRegister };
  },
};
</script>
