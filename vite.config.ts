import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      '@/components': path.resolve(__dirname, './src/components'),
      '@/pages': path.resolve(__dirname, './src/pages'), 
      '@/hooks': path.resolve(__dirname, './src/hooks'),
      '@/services': path.resolve(__dirname, './src/services'),
      '@/stores': path.resolve(__dirname, './src/stores'),
      '@/types': path.resolve(__dirname, './src/types'),
      '@/utils': path.resolve(__dirname, './src/utils'),
      '@/config': path.resolve(__dirname, './src/config'),    // ← Added this
      '@/routes': path.resolve(__dirname, './src/routes'),    // ← Added this
    },
  },
  server: {
    host: '0.0.0.0',
    port: 3000,
    strictPort: true,
    watch: {
      usePolling: true,
      interval: 1000,
      ignored: [
        '**/node_modules/**',
        '**/.env*',
        '**/vite.config.*', 
        '**/tsconfig.*',
        '**/package*.json',
        '**/.git/**',
        '**/dist/**',
        '**/build/**'
      ]
    },
    hmr: {
      port: 3000,
      host: '0.0.0.0'
    }
  },
  optimizeDeps: {
    include: ['react', 'react-dom']
  }
})
