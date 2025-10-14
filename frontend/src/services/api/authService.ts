import apiClient from './client';
import type{
  LoginCredentials,
  RegisterData,
  LoginResponse,
  ApiResponse,
  ChangePasswordRequest,
} from '@/types';

export const authService = {
  /**
   * Login user
   */
  login: async (credentials: LoginCredentials): Promise<LoginResponse> => {
    const response = await apiClient.post<ApiResponse<LoginResponse>>(
      '/auth/login',
      credentials
    );
    return response.data.data;
  },

  /**
   * Register new user
   */
  register: async (data: RegisterData): Promise<void> => {
    await apiClient.post<ApiResponse<void>>('/auth/register', data);
  },

  /**
   * Logout user
   */
  logout: async (): Promise<void> => {
    await apiClient.post<ApiResponse<void>>('/auth/logout');
  },

  /**
   * Refresh access token
   */
  refreshToken: async (refreshToken: string): Promise<LoginResponse> => {
    const response = await apiClient.post<ApiResponse<LoginResponse>>(
      '/auth/refresh',
      null,
      {
        params: { refreshToken },
      }
    );
    return response.data.data;
  },

  /**
   * Verify email with token
   */
  verifyEmail: async (token: string): Promise<void> => {
    await apiClient.get<ApiResponse<void>>('/auth/verify-email', {
      params: { token },
    });
  },

  /**
   * Resend verification email
   */
  resendVerificationEmail: async (email: string): Promise<void> => {
    await apiClient.post<ApiResponse<void>>('/auth/resend-verification', null, {
      params: { email },
    });
  },

  /**
   * Request password reset
   */
  forgotPassword: async (email: string): Promise<void> => {
    await apiClient.post<ApiResponse<void>>('/auth/forgot-password', null, {
      params: { email },
    });
  },

  /**
   * Reset password with token
   */
  resetPassword: async (token: string, newPassword: string): Promise<void> => {
    await apiClient.post<ApiResponse<void>>('/auth/reset-password', null, {
      params: { token, newPassword },
    });
  },

  /**
   * Change password for authenticated user
   */
  changePassword: async (data: ChangePasswordRequest): Promise<void> => {
    await apiClient.post<ApiResponse<void>>('/auth/change-password', data);
  },
};