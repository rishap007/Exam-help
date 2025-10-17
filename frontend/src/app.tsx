import { useEffect } from 'react';
import { QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { BrowserRouter } from 'react-router-dom';
import { Toaster } from 'sonner';
import { queryClient } from '@/config/queryClient';
import { useUIStore } from '@/stores/uiStore';
// import { useAuthStore } from '@/stores/authStore';
import AppRoutes from '@/routes';

function App() {
  // console.log('🔥 MAIN APP LOADED:', new Date().toLocaleTimeString());
  
  // const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  // const user = useAuthStore((state) => state.user);
  const theme = useUIStore((state) => state.theme);
  const setTheme = useUIStore((state) => state.setTheme);

  useEffect(() => {
    setTheme(theme);
  }, []); 

  useEffect(() => {
    if (theme === 'system') {
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
      
      const handleChange = (e: MediaQueryListEvent) => {
        document.documentElement.classList.toggle('dark', e.matches);
      };

      mediaQuery.addEventListener('change', handleChange);
      return () => mediaQuery.removeEventListener('change', handleChange);
    }
  }, [theme]);

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>


        {/* 🔍 TEST: Try AppRoutes */}
        <div style={{ marginTop: '50px' }}>
          <AppRoutes />
        </div>
        
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            classNames: {
              error: 'bg-destructive text-destructive-foreground',
              success: 'bg-green-600 text-white',
              warning: 'bg-yellow-600 text-white',
              info: 'bg-blue-600 text-white',
            },
          }}
        />
      </BrowserRouter>
      {import.meta.env.DEV && <ReactQueryDevtools initialIsOpen={false} />}
    </QueryClientProvider>
  );
}

export default App;
