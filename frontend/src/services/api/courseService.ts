import apiClient from './client';
import type{
  Course,
  CourseStats,
  CreateCourseRequest,
  ApiResponse,
  PageResponse,
} from '@/types';

export const courseService = {
  /**
   * Get published courses with pagination
   */
  getPublishedCourses: async (
    page: number = 0,
    size: number = 20,
    params?: Record<string, any>
  ): Promise<PageResponse<Course>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<Course>>>(
      '/courses',
      {
        params: { page, size, ...params },
      }
    );
    return response.data.data;
  },

  /**
   * Get course by slug
   */
  getCourseBySlug: async (slug: string): Promise<Course> => {
    const response = await apiClient.get<ApiResponse<Course>>(
      `/courses/slug/${slug}`
    );
    return response.data.data;
  },

  /**
   * Create new course (Instructor/Admin)
   */
  createCourse: async (data: CreateCourseRequest): Promise<Course> => {
    const response = await apiClient.post<ApiResponse<Course>>(
      '/courses',
      data
    );
    return response.data.data;
  },

  /**
   * Update course (Owner/Admin)
   */
  updateCourse: async (
    courseId: string,
    data: Partial<CreateCourseRequest>
  ): Promise<Course> => {
    const response = await apiClient.put<ApiResponse<Course>>(
      `/courses/${courseId}`,
      data
    );
    return response.data.data;
  },

  /**
   * Upload course thumbnail
   */
  uploadThumbnail: async (courseId: string, file: File): Promise<Course> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await apiClient.post<ApiResponse<Course>>(
      `/courses/${courseId}/thumbnail`,
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
   * Publish course (Owner/Admin)
   */
  publishCourse: async (courseId: string): Promise<Course> => {
    const response = await apiClient.post<ApiResponse<Course>>(
      `/courses/${courseId}/publish`
    );
    return response.data.data;
  },

  /**
   * Get course statistics (Owner/Admin)
   */
  getCourseStats: async (courseId: string): Promise<CourseStats> => {
    const response = await apiClient.get<ApiResponse<CourseStats>>(
      `/courses/${courseId}/stats`
    );
    return response.data.data;
  },

  /**
   * Delete course (Owner/Admin)
   */
  deleteCourse: async (courseId: string): Promise<void> => {
    await apiClient.delete<ApiResponse<void>>(`/courses/${courseId}`);
  },

  /**
   * Search courses
   */
  searchCourses: async (
    query: string,
    page: number = 0,
    size: number = 20,
    filters?: {
      level?: string;
      categoryId?: string;
      minPrice?: number;
      maxPrice?: number;
    }
  ): Promise<PageResponse<Course>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<Course>>>(
      '/courses/search',
      {
        params: {
          q: query,
          page,
          size,
          ...filters,
        },
      }
    );
    return response.data.data;
  },
};