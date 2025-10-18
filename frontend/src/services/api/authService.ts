import apiClient from './client';
import type {
  LoginCredentials,
  RegisterData,
  LoginResponse,
  // ApiResponse,
  ChangePasswordRequest,
} from '@/types';

export const authService = {
  /**
   * Login user
   */
  login: async (credentials: LoginCredentials): Promise<LoginResponse> => {
    try {
      console.log('🔵 authService.login - Starting with credentials:', credentials);

      const response = await apiClient.post<any>('/auth/login', credentials);

      console.log('🟢 authService.login - Full axios response:', response);
      console.log('🟢 authService.login - response.data:', response.data);
      console.log('🟢 authService.login - response.data type:', typeof response.data);
      console.log('🟢 authService.login - response.data keys:', response.data ? Object.keys(response.data) : 'null');

      // Check if response.data has the expected structure
      if (response.data && response.data.accessToken) {
        console.log('🟢 authService.login - Found accessToken in response.data');
      } else {
        console.log('🔴 authService.login - No accessToken in response.data');
      }

      const result = response.data;
      console.log('🟢 authService.login - Returning result:', result);
      console.log('🟢 authService.login - Result type:', typeof result);

      // Check if there's double nesting
      if (result && result.data && result.data.accessToken) {
        return result.data; // Return the inner data object
      } else if (result && result.accessToken) {
        return result; // Return as-is if accessToken is at top level
      } else {
        console.error('🔴 Unexpected response structure:', result);
        throw new Error('Invalid response format');
      }

    } catch (error) {
      console.error('🔴 authService.login - Error caught:', error);
      throw error;
    }
  },

  // ... rest of your methods (keep them as they are)
  register: async (data: RegisterData): Promise<void> => {
    await apiClient.post<any>('/auth/register', data);
  },

  logout: async (): Promise<void> => {
    await apiClient.post<any>('/auth/logout');
  },

  refreshToken: async (refreshToken: string): Promise<LoginResponse> => {
    const response = await apiClient.post<any>('/auth/refresh', null, {
      params: { refreshToken },
    });
    return response.data;
  },

  verifyEmail: async (token: string): Promise<void> => {
    await apiClient.get<any>('/auth/verify-email', {
      params: { token },
    });
  },

  resendVerificationEmail: async (email: string): Promise<void> => {
    await apiClient.post<any>('/auth/resend-verification', null, {
      params: { email },
    });
  },

  forgotPassword: async (email: string): Promise<void> => {
    await apiClient.post<any>('/auth/forgot-password', null, {
      params: { email },
    });
  },

  resetPassword: async (token: string, newPassword: string): Promise<void> => {
    await apiClient.post<any>('/auth/reset-password', null, {
      params: { token, newPassword },
    });
  },

  changePassword: async (data: ChangePasswordRequest): Promise<void> => {
    await apiClient.post<any>('/auth/change-password', data);
  },
};
