import { useQuery, useMutation, useQueryClient,keepPreviousData } from '@tanstack/react-query';
import { toast } from 'sonner';
import { courseService } from '@/services/api/courseService';
import { queryKeys } from '@/config/queryClient';
import type { CreateCourseRequest } from '@/types';
// import { useQuery, keepPreviousData } from '@tanstack/react-query';


/**
 * Hook for fetching published courses
 */
export const useCourses = (
  page: number = 0,
  size: number = 20,
  params?: Record<string, any>
) => {
  return useQuery({
    queryKey: queryKeys.courses.list({ page, size, ...params }),
    queryFn: () => courseService.getPublishedCourses(page, size, params),
    placeholderData: keepPreviousData, // ✅ v5 syntax
  });
};

/**
 * Hook for fetching a single course by slug
 */
export const useCourse = (slug: string) => {
  return useQuery({
    queryKey: queryKeys.courses.detail(slug),
    queryFn: () => courseService.getCourseBySlug(slug),
    enabled: !!slug,
  });
};

/**
 * Hook for creating a new course
 */
export const useCreateCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateCourseRequest) => courseService.createCourse(data),
    onSuccess: (newCourse) => {
      // Invalidate courses list
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.all });
      
      toast.success('Course created successfully!', {
        description: `${newCourse.title} has been created.`,
      });
    },
    onError: () => {
      toast.error('Failed to create course', {
        description: 'Please try again or contact support.',
      });
    },
  });
};

/**
 * Hook for updating a course
 */
export const useUpdateCourse = (courseId: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: Partial<CreateCourseRequest>) =>
      courseService.updateCourse(courseId, data),
    onSuccess: (updatedCourse) => {
      // Invalidate specific course and list
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.detail(updatedCourse.slug) });
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.all });
      
      toast.success('Course updated successfully!');
    },
    onError: () => {
      toast.error('Failed to update course');
    },
  });
};

/**
 * Hook for uploading course thumbnail
 */
export const useUploadCourseThumbnail = (courseId: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (file: File) => courseService.uploadThumbnail(courseId, file),
    onSuccess: (updatedCourse) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.detail(updatedCourse.slug) });
      toast.success('Thumbnail uploaded successfully!');
    },
    onError: () => {
      toast.error('Failed to upload thumbnail');
    },
  });
};

/**
 * Hook for publishing a course
 */
export const usePublishCourse = (courseId: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => courseService.publishCourse(courseId),
    onSuccess: (publishedCourse) => {
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.detail(publishedCourse.slug) });
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.all });
      
      toast.success('Course published successfully!', {
        description: 'Your course is now visible to students.',
      });
    },
    onError: () => {
      toast.error('Failed to publish course');
    },
  });
};

/**
 * Hook for getting course statistics
 */
export const useCourseStats = (courseId: string) => {
  return useQuery({
    queryKey: queryKeys.courses.stats(courseId),
    queryFn: () => courseService.getCourseStats(courseId),
    enabled: !!courseId,
  });
};

/**
 * Hook for deleting a course
 */
export const useDeleteCourse = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (courseId: string) => courseService.deleteCourse(courseId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.courses.all });
      toast.success('Course deleted successfully');
    },
    onError: () => {
      toast.error('Failed to delete course');
    },
  });
};

/**
 * Hook for searching courses
 */
export const useSearchCourses = (
  query: string,
  page: number = 0,
  size: number = 20,
  filters?: Record<string, any>
) => {
  return useQuery({
    queryKey: ['courses', 'search', query, page, size, filters],
    queryFn: () => courseService.searchCourses(query, page, size, filters),
    enabled: query.length > 0,
    placeholderData: keepPreviousData,
  });
};