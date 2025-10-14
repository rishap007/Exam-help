import apiClient from './client';
import type{
  User,
  UserStats,
  UpdateProfileRequest,
  ApiResponse,
  PageResponse,
  UserStatus,
} from '@/types';

export const userService = {
  /**
   * Get current user profile
   */
  getCurrentProfile: async (): Promise<User> => {
    const response = await apiClient.get<ApiResponse<User>>('/users/profile');
    return response.data.data;
  },

  /**
   * Update current user profile
   */
  updateProfile: async (data: UpdateProfileRequest): Promise<User> => {
    const response = await apiClient.put<ApiResponse<User>>(
      '/users/profile',
      data
    );
    return response.data.data;
  },

  /**
   * Upload profile picture
   */
  uploadProfilePicture: async (file: File): Promise<User> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await apiClient.post<ApiResponse<User>>(
      '/users/profile/picture',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );
    return response.data.data;
  },

  /**
   * Get user by ID
   */
  getUserById: async (userId: string): Promise<User> => {
    const response = await apiClient.get<ApiResponse<User>>(
      `/users/${userId}`
    );
    return response.data.data;
  },

  /**
   * Get all users (Admin only)
   */
  getAllUsers: async (
    page: number = 0,
    size: number = 20
  ): Promise<PageResponse<User>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<User>>>(
      '/users',
      {
        params: { page, size },
      }
    );
    return response.data.data;
  },

  /**
   * Search users (Admin only)
   */
  searchUsers: async (
    searchTerm: string,
    page: number = 0,
    size: number = 20
  ): Promise<PageResponse<User>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<User>>>(
      '/users/search',
      {
        params: { searchTerm, page, size },
      }
    );
    return response.data.data;
  },

  /**
   * Update user status (Admin only)
   */
  updateUserStatus: async (
    userId: string,
    status: UserStatus
  ): Promise<User> => {
    const response = await apiClient.put<ApiResponse<User>>(
      `/users/${userId}/status`,
      null,
      {
        params: { status },
      }
    );
    return response.data.data;
  },

  /**
   * Get user statistics
   */
  getUserStats: async (userId: string): Promise<UserStats> => {
    const response = await apiClient.get<ApiResponse<UserStats>>(
      `/users/${userId}/stats`
    );
    return response.data.data;
  },

  /**
   * Delete user (Admin only)
   */
  deleteUser: async (userId: string): Promise<void> => {
    await apiClient.delete<ApiResponse<void>>(`/users/${userId}`);
  },
};