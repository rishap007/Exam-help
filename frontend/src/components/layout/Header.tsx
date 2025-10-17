import { Link } from 'react-router-dom';
import { Menu, Bell, Search, Moon, Sun, LogOut, User, Settings } from 'lucide-react';
import { useAuthStore } from '@/stores/authStore';
import { useUIStore } from '@/stores/uiStore';
import { useLogout } from '@/hooks/useAuth';
import { Avatar } from '@/components/ui/Avatar';
import { Button } from '@/components/ui/Button';
import { Badge } from '@/components/ui/Badge';
import { useState } from 'react';
// import { cn } from '@/utils/cn';

export const Header = () => {
  const user = useAuthStore((state) => state.user);
  const theme = useUIStore((state) => state.theme);
  const toggleTheme = useUIStore((state) => state.toggleTheme);
  const toggleSidebar = useUIStore((state) => state.toggleSidebar);
  const toggleNotificationsPanel = useUIStore(
    (state) => state.toggleNotificationsPanel
  );

  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const logout = useLogout();

  const handleLogout = () => {
    logout.mutate();
  };

  const getThemeIcon = () => {
    if (theme === 'dark') return <Moon className="h-5 w-5" />;
    if (theme === 'light') return <Sun className="h-5 w-5" />;
    return <Sun className="h-5 w-5" />;
  };

  return (
    <header className="sticky top-0 z-40 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="flex h-16 items-center px-4 md:px-6">
        {/* Left section */}
        <div className="flex items-center gap-4">
          <Button
            variant="ghost"
            size="icon"
            onClick={toggleSidebar}
            className="md:hidden"
          >
            <Menu className="h-5 w-5" />
          </Button>

          <Link to="/" className="flex items-center gap-2">
            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary text-primary-foreground font-bold">
              EP
            </div>
            <span className="hidden font-bold sm:inline-block">EduPlatform</span>
          </Link>
        </div>

        {/* Center section - Search */}
        <div className="flex-1 px-4">
          <div className="relative max-w-md">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <input
              type="search"
              placeholder="Search courses..."
              className="h-9 w-full rounded-md border border-input bg-background pl-9 pr-3 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
            />
          </div>
        </div>

        {/* Right section */}
        <div className="flex items-center gap-2">
          {/* Theme toggle */}
          <Button variant="ghost" size="icon" onClick={toggleTheme}>
            {getThemeIcon()}
          </Button>

          {/* Notifications */}
          {user && (
            <div className="relative">
              <Button
                variant="ghost"
                size="icon"
                onClick={toggleNotificationsPanel}
              >
                <Bell className="h-5 w-5" />
                <Badge
                  variant="destructive"
                  className="absolute -right-1 -top-1 h-5 w-5 rounded-full p-0 text-xs"
                >
                  3
                </Badge>
              </Button>
            </div>
          )}

          {/* User menu */}
          {user ? (
            <div className="relative">
              <button
                onClick={() => setShowProfileMenu(!showProfileMenu)}
                className="flex items-center gap-2 rounded-md p-2 hover:bg-accent"
              >
                <Avatar
                  src={user.profilePictureUrl}
                  fallback={`${user.firstName} ${user.lastName}`}
                  size="sm"
                />
                <span className="hidden text-sm font-medium md:inline-block">
                  {user.firstName}
                </span>
              </button>

              {/* Dropdown menu */}
              {showProfileMenu && (
                <>
                  <div
                    className="fixed inset-0 z-40"
                    onClick={() => setShowProfileMenu(false)}
                  />
                  <div className="absolute right-0 top-full z-50 mt-2 w-56 rounded-md border bg-popover p-1 shadow-md">
                    <div className="px-2 py-1.5">
                      <p className="text-sm font-medium">{`${user.firstName} ${user.lastName}`}</p>
                      <p className="text-xs text-muted-foreground">
                        {user.email}
                      </p>
                    </div>
                    <div className="h-px bg-border my-1" />
                    <Link
                      to="/profile"
                      className="flex items-center gap-2 rounded-sm px-2 py-1.5 text-sm hover:bg-accent"
                      onClick={() => setShowProfileMenu(false)}
                    >
                      <User className="h-4 w-4" />
                      Profile
                    </Link>
                    <Link
                      to="/settings"
                      className="flex items-center gap-2 rounded-sm px-2 py-1.5 text-sm hover:bg-accent"
                      onClick={() => setShowProfileMenu(false)}
                    >
                      <Settings className="h-4 w-4" />
                      Settings
                    </Link>
                    <div className="h-px bg-border my-1" />
                    <button
                      onClick={handleLogout}
                      className="flex w-full items-center gap-2 rounded-sm px-2 py-1.5 text-sm text-destructive hover:bg-accent"
                    >
                      <LogOut className="h-4 w-4" />
                      Logout
                    </button>
                  </div>
                </>
              )}
            </div>
          ) : (
            <div className="flex items-center gap-2">
              <Link to="/login">
                <Button variant="ghost">Login</Button>
              </Link>
              <Link to="/register">
                <Button>Sign Up</Button>
              </Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};