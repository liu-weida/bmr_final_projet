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
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  setup() {
    const username = ref('');
    const password = ref('');
    const errorMessage = ref('');
    const router = useRouter();

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

        if (!data || !data.data || !data.data.token) {
          throw new Error('响应格式不正确，缺少 token');
        }

        // 存储 token 和用户名
        localStorage.setItem('token', data.data.token);
        localStorage.setItem('username', username.value);

        // 跳转到对应的用户名页面
        router.push(`/${username.value}`);
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
