import { useEffect } from 'react';
import { QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { BrowserRouter } from 'react-router-dom';
import { Toaster } from 'sonner';
import { queryClient } from '@/config/queryClient';
import { useUIStore } from '@/stores/uiStore';
import { useAuthStore } from '@/stores/authStore';
import AppRoutes from '@/routes';

function App() {
  console.log('🔥 MAIN APP LOADED:', new Date().toLocaleTimeString());
  
  // 🔍 DEBUG: Check authentication state
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const user = useAuthStore((state) => state.user);
  console.log('🔍 AUTH STATE:', { isAuthenticated, user });
  console.log('🔍 CURRENT URL:', window.location.pathname);
  
  const theme = useUIStore((state) => state.theme);
  const setTheme = useUIStore((state) => state.setTheme);

  // Initialize theme on mount
  useEffect(() => {
    setTheme(theme);
  }, []);

  // Listen for system theme changes
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
        
        {/* 🚨 RED TEST BOX - REMOVE THIS AFTER TESTING */}
        <div style={{
          background: 'red',
          color: 'white',
          padding: '15px',
          fontSize: '18px',
          position: 'fixed',
          top: '0',
          left: '0',
          zIndex: 9999,
          width: '100%',
          textAlign: 'center'
        }}>
          🚨 APP TEST: {new Date().toLocaleTimeString()} | 
          Auth: {isAuthenticated ? 'YES' : 'NO'} | 
          URL: {window.location.pathname}
        </div>

        {/* 🔍 DEBUG INFO BOX */}
        <div style={{
          background: 'blue',
          color: 'white',
          padding: '10px',
          fontSize: '14px',
          position: 'fixed',
          top: '60px',
          left: '0',
          zIndex: 9998,
          width: '100%',
          textAlign: 'left'
        }}>
          🔍 DEBUG: Auth={String(isAuthenticated)} | User={user ? `${user.role} - ${user.firstName}` : 'null'}
        </div>

        <div style={{ marginTop: '120px' }}>
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
