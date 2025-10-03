import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { authService } from '@/services/api/authService';
import { userService } from '@/services/api/userService';
import { useAuthStore } from '@/stores/authStore';
import { queryKeys } from '@/config/queryClient';
import type {
  LoginCredentials,
  RegisterData,
  ChangePasswordRequest,
  User,
} from '@/types';

/**
 * Hook for user login
 */
export const useLogin = () => {
  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);

  return useMutation<
    Awaited<ReturnType<typeof authService.login>>,
    Error,
    LoginCredentials
  >({
    mutationFn: (credentials) => authService.login(credentials),
    onSuccess: (data) => {
      setAuth(data);
      toast.success('Welcome back!', {
        description: `Logged in as ${data.user.firstName} ${data.user.lastName}`,
      });

      const dashboardPath =
        data.user.role === 'ADMIN'
          ? '/admin/dashboard'
          : data.user.role === 'INSTRUCTOR'
          ? '/instructor/dashboard'
          : '/dashboard';

      navigate(dashboardPath);
    },
    onError: () => {
      toast.error('Login failed', {
        description: 'Please check your credentials and try again.',
      });
    },
  });
};

/**
 * Hook for user registration
 */
export const useRegister = () => {
  const navigate = useNavigate();

  return useMutation<
    Awaited<ReturnType<typeof authService.register>>,
    Error,
    RegisterData
  >({
    mutationFn: (data) => authService.register(data),
    onSuccess: () => {
      toast.success('Registration successful!', {
        description: 'Please check your email to verify your account.',
      });
      navigate('/login');
    },
    onError: () => {
      toast.error('Registration failed', {
        description: 'Please try again or contact support.',
      });
    },
  });
};

/**
 * Hook for user logout
 */
export const useLogout = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const logout = useAuthStore((state) => state.logout);

  return useMutation<void, Error>({
    mutationFn: () => authService.logout(),
    onSuccess: () => {
      logout();
      queryClient.clear();
      toast.success('Logged out successfully');
      navigate('/login');
    },
    onError: () => {
      logout();
      queryClient.clear();
      navigate('/login');
    },
  });
};

/**
 * Hook for email verification
 */
export const useVerifyEmail = () => {
  const navigate = useNavigate();

  return useMutation<void, Error, string>({
    mutationFn: (token) => authService.verifyEmail(token),
    onSuccess: () => {
      toast.success('Email verified successfully!', {
        description: 'You can now log in to your account.',
      });
      navigate('/login');
    },
    onError: () => {
      toast.error('Verification failed', {
        description: 'The verification link may have expired.',
      });
    },
  });
};

/**
 * Hook for resending verification email
 */
export const useResendVerification = () => {
  return useMutation<void, Error, string>({
    mutationFn: (email) => authService.resendVerificationEmail(email),
    onSuccess: () => {
      toast.success('Verification email sent!', {
        description: 'Please check your inbox.',
      });
    },
  });
};

/**
 * Hook for forgot password
 */
export const useForgotPassword = () => {
  return useMutation<void, Error, string>({
    mutationFn: (email) => authService.forgotPassword(email),
    onSuccess: () => {
      toast.success('Password reset email sent!', {
        description: 'Please check your inbox for instructions.',
      });
    },
  });
};

/**
 * Hook for reset password
 */
export const useResetPassword = () => {
  const navigate = useNavigate();

  return useMutation<
    void,
    Error,
    { token: string; newPassword: string }
  >({
    mutationFn: ({ token, newPassword }) =>
      authService.resetPassword(token, newPassword),
    onSuccess: () => {
      toast.success('Password reset successfully!', {
        description: 'You can now log in with your new password.',
      });
      navigate('/login');
    },
    onError: () => {
      toast.error('Password reset failed', {
        description: 'The reset link may have expired.',
      });
    },
  });
};

/**
 * Hook for change password
 */
export const useChangePassword = () => {
  return useMutation<void, Error, ChangePasswordRequest>({
    mutationFn: (data) => authService.changePassword(data),
    onSuccess: () => {
      toast.success('Password changed successfully!');
    },
  });
};

/**
 * Hook for getting current user
 */
export const useCurrentUser = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const setUser = useAuthStore((state) => state.setUser);

  return useQuery<User, Error>({
    queryKey: queryKeys.auth.profile,
    queryFn: userService.getCurrentProfile,
    
    enabled: isAuthenticated,
    staleTime: 10 * 60 * 1000, // 10 minutes
    onSuccess: (data:User) => {
      setUser(data);
    },
  });
};

/**
 * Hook to check if user has specific role
 */
export const useHasRole = (roles: string | string[]) => {
  const user = useAuthStore((state) => state.user);

  if (!user) return false;

  const allowedRoles = Array.isArray(roles) ? roles : [roles];
  return allowedRoles.includes(user.role);
};

/**
 * Hook to check if user is authenticated
 */
export const useIsAuthenticated = () => {
  return useAuthStore((state) => state.isAuthenticated);
};
