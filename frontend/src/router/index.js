import { createRouter, createWebHistory } from 'vue-router';
import Welcome from '../views/Welcome.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';

const routes = [
  {
    path: '/',
    name: 'Welcome',
    component: Welcome,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
