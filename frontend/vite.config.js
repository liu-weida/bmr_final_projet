import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000, // Port for the frontend
    proxy: {
        '/api/bmr/user': {
            target: 'http://46qnjj327962.vicp.fun:37122',
            changeOrigin: true,
        },
        '/api/bmr/project': {
            target: 'http://46qnjj327962.vicp.fun:58857',
            changeOrigin: true,
        },
    },
  },
});
