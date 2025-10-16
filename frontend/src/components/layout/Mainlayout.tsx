import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Sidebar } from './Sidebar';
import { useUIStore } from '@/stores/uiStore';
import { cn } from '@/utils/cn';

export const MainLayout = () => {
  const sidebarCollapsed = useUIStore((state) => state.sidebarCollapsed);

  return (
    <div className="relative min-h-screen">
      <Header />
      <Sidebar />
      
      <main
        className={cn(
          'min-h-[calc(100vh-4rem)] pt-16 transition-all duration-300',
          sidebarCollapsed ? 'md:pl-16' : 'md:pl-64'
        )}
      >
        <div className="container mx-auto p-4 md:p-6">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

// Simple layout without sidebar (for auth pages, etc.)
export const SimpleLayout = () => {
  return (
    <div className="relative min-h-screen">
      <Header />
      <main className="min-h-[calc(100vh-4rem)] pt-16">
        <Outlet />
      </main>
    </div>
  );
};