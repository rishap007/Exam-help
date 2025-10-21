import { Routes, Route, Navigate, Link } from 'react-router-dom';
import { useAuthStore } from '@/stores/authStore';
import { UserRole } from '@/types';
import { MainLayout, SimpleLayout } from '@/components/layout/MainLayout';

// Auth pages
import { LoginPage } from '@/pages/auth/LoginPage';
import { RegisterPage } from '@/pages/auth/RegisterPage';
import { ForgotPasswordPage } from '@/pages/auth/ForgotPasswordPage';
import { ResetPasswordPage } from '@/pages/auth/ResetPasswordPage';
import { VerifyEmailPage } from '@/pages/auth/VerifyEmailPage';

// Dashboard pages
import { StudentDashboard } from '@/pages/dashboard/StudentDashboard';
import { InstructorDashboard } from '@/pages/dashboard/InstructorDashboard';

// Course pages
import { CoursesListPage } from '@/pages/courses/CourseListPage';
import { CourseDetailPage } from '@/pages/courses/CourseDetailPage';
import { MyCoursesPage } from '@/pages/courses/MyCoursesPage';
import { HomePage } from '@/pages/home/HomePage';

// Placeholder components
const AdminDashboard = () => <div className="p-8">Admin Dashboard - Coming Soon</div>;
const ProfilePage = () => <div className="p-8">Profile Page - Coming Soon</div>;
const SettingsPage = () => <div className="p-8">Settings - Coming Soon</div>;

// const HomePage = () => (
//   <div className="flex h-screen items-center justify-center">
//     <div className="text-center">
//       <h1 className="text-4xl font-bold text-gradient">EduPlatform</h1>
//       <p className="mt-4 text-muted-foreground">
//         Your Learning Management System
//       </p>
//       <div className="mt-8 space-x-4">
//         {/* ✅ Fixed: Use Link instead of <a> */}
//         <Link
//           to="/login"
//           className="rounded-md bg-primary px-6 py-3 text-white hover:bg-primary/90 inline-block"
//         >
//           Login
//         </Link>
//         <Link
//           to="/register"
//           className="rounded-md border border-primary px-6 py-3 text-primary hover:bg-primary/10 inline-block"
//         >
//           Register
//         </Link>
//       </div>
//     </div>
//   </div>
// );

// Protected Route Component
interface ProtectedRouteProps {
  children: React.ReactNode;
  roles?: UserRole[];
}

const ProtectedRoute = ({ children, roles }: ProtectedRouteProps) => {
  const user = useAuthStore((state) => state.user);
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Check role-based access
  if (roles && user && !roles.includes(user.role)) {
    // ✅ Better redirect logic
    const dashboardPath =
      user.role === UserRole.ADMIN
        ? '/admin/dashboard'
        : user.role === UserRole.INSTRUCTOR
        ? '/instructor/dashboard'
        : '/dashboard';
    return <Navigate to={dashboardPath} replace />;
  }

  return <>{children}</>;
};

// Public Route Component (redirect if authenticated)
const PublicRoute = ({ children }: { children: React.ReactNode }) => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const user = useAuthStore((state) => state.user);

  if (isAuthenticated && user) {
    // Redirect based on role
    const dashboardPath =
      user.role === UserRole.ADMIN
        ? '/admin/dashboard'
        : user.role === UserRole.INSTRUCTOR
        ? '/instructor/dashboard'
        : '/dashboard';
    
    return <Navigate to={dashboardPath} replace />;
  }

  return <>{children}</>;
};

// Dashboard router - shows appropriate dashboard based on role
const DashboardRouter = () => {
  const user = useAuthStore((state) => state.user);

  if (!user) return <Navigate to="/login" replace />;

  switch (user.role) {
    case UserRole.ADMIN:
      return <Navigate to="/admin/dashboard" replace />; // ✅ Redirect to specific admin route
    case UserRole.INSTRUCTOR:
      return <Navigate to="/instructor/dashboard" replace />; // ✅ Redirect to specific instructor route
    case UserRole.STUDENT:
    default:
      return <StudentDashboard />;
  }
};

const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Routes - No Layout */}
      <Route path="/" element={<HomePage />} />

      {/* Auth Routes - Simple Layout */}
      <Route element={<SimpleLayout />}>
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
        <Route
          path="/forgot-password"
          element={
            <PublicRoute>
              <ForgotPasswordPage />
            </PublicRoute>
          }
        />
        <Route
          path="/reset-password"
          element={
            <PublicRoute>
              <ResetPasswordPage />
            </PublicRoute>
          }
        />
        <Route path="/verify-email" element={<VerifyEmailPage />} />
      </Route>

      {/* Protected Routes - Main Layout with Sidebar */}
      <Route
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        {/* Dashboard - Role-based routing */}
        <Route path="/dashboard" element={<DashboardRouter />} />

        {/* Student Routes */}
        <Route
          path="/my-courses"
          element={
            <ProtectedRoute roles={[UserRole.STUDENT]}>
              <MyCoursesPage />
            </ProtectedRoute>
          }
        />

        {/* Instructor Routes */}
        <Route
          path="/instructor/dashboard"
          element={
            <ProtectedRoute roles={[UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <InstructorDashboard />
            </ProtectedRoute>
          }
        />

        {/* Admin Routes */}
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute roles={[UserRole.ADMIN]}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        {/* Common Routes */}
        {/* ✅ Fixed: Use correct component name */}
        <Route path="/courses" element={<CoursesListPage />} />
        <Route path="/courses/:slug" element={<CourseDetailPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/settings" element={<SettingsPage />} />
      </Route>

      {/* 404 - Not Found */}
      <Route
        path="*"
        element={
          <div className="flex h-screen items-center justify-center">
            <div className="text-center">
              <h1 className="text-6xl font-bold">404</h1>
              <p className="mt-4 text-muted-foreground">Page not found</p>
              {/* ✅ Fixed: Use Link instead of <a> */}
              <Link
                to="/"
                className="mt-8 inline-block rounded-md bg-primary px-6 py-3 text-white hover:bg-primary/90"
              >
                Go Home
              </Link>
            </div>
          </div>
        }
      />
    </Routes>
  );
};

export default AppRoutes;
