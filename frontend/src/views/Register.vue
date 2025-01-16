<template>
  <div>
    <h2>注册</h2>
    <form @submit.prevent="handleRegister">
      <div>
        <label>用户名：</label>
        <input v-model="username" />
      </div>
      <div>
        <label>密码：</label>
        <input v-model="password" type="password" />
      </div>
      <div>
        <label>邮箱：</label>
        <input v-model="mail" />
      </div>
      <div>
        <label>电话：</label>
        <input v-model="phone" />
      </div>
      <button type="submit">注册</button>
    </form>
    <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'Register',
  setup() {
    const username = ref('');
    const password = ref('');
    const mail = ref('');
    const phone = ref('');
    const errorMessage = ref('');

    function generateRandomLong() {
      const arr = new Uint32Array(2);
      crypto.getRandomValues(arr);
      const high = arr[0];
      const low = arr[1];
      const combined = (BigInt(high) << 32n) | BigInt(low);
      return combined;
    }

    const handleRegister = async () => {
      try {
        const res = await fetch('/api/bmr/user/v1/user', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            id : generateRandomLong(),
            username: username.value,
            password: password.value,
            mail: mail.value,
            phone: phone.value,
          }),
        });

        if (!res.ok) {
          throw new Error(`注册请求失败，状态码：${res.status}`);
        }

        const data = await res.json();
        console.log('注册成功，后端返回：', data);

        // 注册成功后，可以跳转到登录页
        window.location.href = '/login';
      } catch (err) {
        errorMessage.value = err.message;
      }
    };

    return {
      username,
      password,
      mail,
      phone,
      errorMessage,
      handleRegister,
    };
  },
};
</script>
