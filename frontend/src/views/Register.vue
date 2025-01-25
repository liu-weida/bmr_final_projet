<template>
  <div>
    <h2>注册</h2>
    <form @submit.prevent="handleRegister">
      <div>
        <label>用户名：</label>
        <input v-model="username" required />
      </div>
      <div>
        <label>密码：</label>
        <input v-model="password" type="password" required />
      </div>
      <div>
        <label>邮箱：</label>
        <input v-model="mail" type="email" required />
      </div>
      <div>
        <label>电话：</label>
        <input v-model="phone" type="tel" required />
      </div>
      <button type="submit">注册</button>
    </form>
    <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';

// 生成随机 bigint ID
function generateRandomBigInt() {
  return BigInt(Math.floor(Math.random() * 1e16)); // 生成一个 16 位随机数并转换为 bigint
}

export default {
  name: 'Register',
  setup() {
    const username = ref('');
    const password = ref('');
    const mail = ref('');
    const phone = ref('');
    const errorMessage = ref('');

    const handleRegister = async () => {
      try {
        // 生成随机 userId
        const userId = generateRandomBigInt().toString();

        // 调用注册 API
        const registerResponse = await axios.put('/api/bmr/user/v1/user', {
          id: userId,
          username: username.value,
          password: password.value,
          mail: mail.value,
          phone: phone.value,
        });

        if (registerResponse.data.code !== '0') {
          errorMessage.value = `注册失败：${registerResponse.data.message}`;
          return;
        }

        console.log('注册成功：', registerResponse.data);
        console.log('username', username.value);
        console.log('passowrd', password.value);


        // 清空错误消息
        errorMessage.value = '';

        // 跳转到首页或其他页面
        window.location.href = '/login';
      } catch (err) {
        console.error('注册或登录请求发生错误:', err);
        errorMessage.value = '发生未知错误，请稍后重试';
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

<style>
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
