<template>
  <div class="register-container">
    <h1>注册</h1>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="username">用户名：</label>
        <input id="username" v-model="form.username" placeholder="请输入用户名" required />
      </div>
      <div class="form-group">
        <label for="password">密码：</label>
        <input
          id="password"
          type="password"
          v-model="form.password"
          placeholder="请输入密码"
          required
        />
      </div>
      <div class="form-group">
        <label for="mail">邮箱：</label>
        <input id="mail" type="email" v-model="form.mail" placeholder="请输入邮箱" required />
      </div>
      <div class="form-group">
        <label for="phone">电话号码：</label>
        <input
          id="phone"
          type="tel"
          v-model="form.phone"
          placeholder="请输入电话号码"
          required
        />
      </div>
      <button type="submit" :disabled="isSubmitting">注册</button>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const form = ref({
  username: '',
  password: '',
  mail: '',
  phone: '',
});

const isSubmitting = ref(false);
const errorMessage = ref('');
const successMessage = ref('');
const router = useRouter();

function generateRandomNumberString() {
  let result = '';
  const digits = '0123456789';

  for (let i = 0; i < 8; i++) {
    const randomIndex = Math.floor(Math.random() * digits.length);
    result += digits[randomIndex];
  }

  return result;
}

const handleRegister = async () => {
  isSubmitting.value = true;
  errorMessage.value = '';
  successMessage.value = '';

  try {
    const userId = generateRandomNumberString();
    const queryParams = {
        id: parseInt(userId, 10),
        username: form.value.username,
        password: form.value.password,
        mail: form.value.mail,
        phone: form.value.phone,
    };



    console.log(queryParams);

    const response = await axios.post('/api/bmr/user/v1/user', queryParams,
        { headers: { 'Content-Type': 'application/json' } },
    );
    console.log(response);

    if (response.data.code === '0') {
      successMessage.value = '注册成功！正在跳转到登录页面...';
      setTimeout(() => {
        router.push('/login');
      }, 2000);
    } else {
      errorMessage.value = response.data.message || '注册失败，请重试。';
    }
  } catch (error) {
    console.log(error);
    errorMessage.value = '网络错误，请稍后重试。';
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  background: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

input:focus {
  outline: none;
  border-color: #4caf50;
  box-shadow: 0 0 4px rgba(76, 175, 80, 0.5);
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

button:disabled {
  background-color: #a5d6a7;
  cursor: not-allowed;
}

button:hover:enabled {
  background-color: #45a049;
}

.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.success-message {
  color: green;
  margin-top: 10px;
  text-align: center;
}
</style>
