import { Link, useLocation } from 'react-router-dom';
import {
  LayoutDashboard,
  BookOpen,
  GraduationCap,
  Users,
  Settings, 
  FileText,
  BarChart3,
  ChevronLeft,
  ChevronRight,
} from 'lucide-react';
import { useAuthStore } from '@/stores/authStore';
import { useUIStore } from '@/stores/uiStore';
import { cn } from '@/utils/cn';
import { Button } from '@/components/ui/Button';
import { UserRole } from '@/types';

interface NavItem {
  title: string;
  href: string;
  icon: React.ComponentType<{ className?: string }>;
  roles?: UserRole[];
}

const navigationItems: NavItem[] = [
  {
    title: 'Dashboard',
    href: '/dashboard',
    icon: LayoutDashboard,
  },
  {
    title: 'My Courses',
    href: '/my-courses',
    icon: BookOpen,
    roles: [UserRole.STUDENT],
  },
  {
    title: 'Browse Courses',
    href: '/courses',
    icon: GraduationCap,
  },
  {
    title: 'My Teaching',
    href: '/instructor/courses',
    icon: BookOpen,
    roles: [UserRole.INSTRUCTOR, UserRole.ADMIN],
  },
  {
    title: 'Students',
    href: '/instructor/students',
    icon: Users,
    roles: [UserRole.INSTRUCTOR, UserRole.ADMIN],
  },
  {
    title: 'Analytics',
    href: '/instructor/analytics',
    icon: BarChart3,
    roles: [UserRole.INSTRUCTOR, UserRole.ADMIN],
  },
  {
    title: 'User Management',
    href: '/admin/users',
    icon: Users,
    roles: [UserRole.ADMIN],
  },
  {
    title: 'Assignments',
    href: '/assignments',
    icon: FileText,
  },
  {
    title: 'Settings',
    href: '/settings',
    icon: Settings,
  },
];

export const Sidebar = () => {
  const location = useLocation();
  const user = useAuthStore((state) => state.user);
  const sidebarOpen = useUIStore((state) => state.sidebarOpen);
  const sidebarCollapsed = useUIStore((state) => state.sidebarCollapsed);
  const setSidebarOpen = useUIStore((state) => state.setSidebarOpen);
  const toggleSidebarCollapsed = useUIStore(
    (state) => state.toggleSidebarCollapsed
  );

  const filteredNavItems = navigationItems.filter((item) => {
    if (!item.roles) return true;
    return user && item.roles.includes(user.role);
  });

  return (
    <>
      {/* Mobile backdrop */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 z-40 bg-background/80 backdrop-blur-sm md:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={cn(
          'fixed left-0 top-16 z-40 h-[calc(100vh-4rem)] border-r bg-background transition-all duration-300',
          sidebarCollapsed ? 'w-16' : 'w-64',
          sidebarOpen
            ? 'translate-x-0'
            : '-translate-x-full md:translate-x-0'
        )}
      >
        <div className="flex h-full flex-col">
          {/* Navigation */}
          <nav className="flex-1 space-y-1 overflow-y-auto p-2 scrollbar-thin">
            {filteredNavItems.map((item) => {
              const Icon = item.icon;
              const isActive = location.pathname === item.href;

              return (
                <Link
                  key={item.href}
                  to={item.href}
                  className={cn(
                    'flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium transition-colors',
                    isActive
                      ? 'bg-primary text-primary-foreground'
                      : 'text-muted-foreground hover:bg-accent hover:text-accent-foreground',
                    sidebarCollapsed && 'justify-center'
                  )}
                  onClick={() => {
                    if (window.innerWidth < 768) {
                      setSidebarOpen(false);
                    }
                  }}
                  title={sidebarCollapsed ? item.title : undefined}
                >
                  <Icon className="h-5 w-5 shrink-0" />
                  {!sidebarCollapsed && <span>{item.title}</span>}
                </Link>
              );
            })}
          </nav>

          {/* Collapse toggle (desktop only) */}
          <div className="hidden border-t p-2 md:block">
            <Button
              variant="ghost"
              size="sm"
              onClick={toggleSidebarCollapsed}
              className="w-full justify-center"
            >
              {sidebarCollapsed ? (
                <ChevronRight className="h-4 w-4" />
              ) : (
                <>
                  <ChevronLeft className="h-4 w-4" />
                  <span className="ml-2">Collapse</span>
                </>
              )}
            </Button>
          </div>
        </div>
      </aside>
    </>
  );
};