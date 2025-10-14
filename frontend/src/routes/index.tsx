import { Routes, Route, Navigate } from 'react-router-dom';
import { useIsAuthenticated } from '@/hooks/useAuth';

// Placeholder components - we'll create these in the next steps
const LoginPage = () => <div className="p-8">Login Page - Coming Soon</div>;
const RegisterPage = () => <div className="p-8">Register Page - Coming Soon</div>;
const DashboardPage = () => <div className="p-8">Dashboard - Coming Soon</div>;
const HomePage = () => (
  <div className="flex h-screen items-center justify-center">
    <div className="text-center">
      <h1 className="text-4xl font-bold text-gradient">EduPlatform</h1>
      <p className="mt-4 text-muted-foreground">
        Your Learning Management System
      </p>
      <div className="mt-8 space-x-4">
        <a
          href="/login"
          className="rounded-md bg-primary px-6 py-3 text-white hover:bg-primary/90"
        >
          Login
        </a>
        <a
          href="/register"
          className="rounded-md border border-primary px-6 py-3 text-primary hover:bg-primary/10"
        >
          Register
        </a>
      </div>
    </div>
  </div>
);

// Protected Route Component
interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const isAuthenticated = useIsAuthenticated();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

// Public Route Component (redirect if authenticated)
const PublicRoute = ({ children }: ProtectedRouteProps) => {
  const isAuthenticated = useIsAuthenticated();

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  return <>{children}</>;
};

const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<HomePage />} />
      <Route
        path="/login"
        element={
          <PublicRoute>
            <LoginPage />
          </PublicRoute>
        }
      />
      <Route
        path="/register"
        element={
          <PublicRoute>
            <RegisterPage />
          </PublicRoute>
        }
      />

      {/* Protected Routes */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <DashboardPage />
          </ProtectedRoute>
        }
      />

      {/* 404 - Not Found */}
      <Route
        path="*"
        element={
          <div className="flex h-screen items-center justify-center">
            <div className="text-center">
              <h1 className="text-6xl font-bold">404</h1>
              <p className="mt-4 text-muted-foreground">Page not found</p>
              <a
                href="/"
                className="mt-8 inline-block rounded-md bg-primary px-6 py-3 text-white hover:bg-primary/90"
              >
                Go Home
              </a>
            </div>
          </div>
        }
      />
    </Routes>
  );
};

export default AppRoutes;