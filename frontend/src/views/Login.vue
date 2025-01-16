<template>
  <div>
    <h2>登录</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label>用户名：</label>
        <input v-model="username" />
      </div>
      <div>
        <label>密码：</label>
        <input v-model="password" type="password" />
      </div>
      <button type="submit">登录</button>
    </form>
    <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'Login',
  setup() {
    const username = ref('');
    const password = ref('');
    const errorMessage = ref('');

    const handleLogin = async () => {
      try {
        const res = await fetch('/api/bmr/user/v1/user/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            username: username.value,
            password: password.value,
          }),
        });

        if (!res.ok) {
          throw new Error(`请求失败，状态码：${res.status}`);
        }

        const data = await res.json();
        console.log('登录成功后端返回：', data);

        // 登录成功后跳转或者存储 token、用户信息等
        // 假设后端返回的数据中有一个 token
        // localStorage.setItem('token', data.token);

        // 跳转到首页或者其他页面
        window.location.href = '/';
      } catch (err) {
        errorMessage.value = err.message;
      }
    };

    return {
      username,
      password,
      errorMessage,
      handleLogin,
    };
  },
};
</script>
