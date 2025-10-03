import { QueryClient, QueryCache, MutationCache } from '@tanstack/react-query';
import { toast } from 'sonner';
import type{ ApiError } from '@/types';

// Error handler for queries
const handleQueryError = (error: unknown) => {
  const apiError = error as ApiError;
  
  // Don't show toast for 401 errors (handled by axios interceptor)
  if (apiError.status === 401) {
    return;
  }

  // Show error toast
  toast.error(apiError.message || 'An error occurred', {
    description: apiError.errors
      ? Object.entries(apiError.errors)
          .map(([field, messages]) => `${field}: ${messages.join(', ')}`)
          .join('\n')
      : undefined,
  });
};

// Error handler for mutations
const handleMutationError = (error: unknown) => {
  const apiError = error as ApiError;
  
  // Don't show toast for 401 errors (handled by axios interceptor)
  if (apiError.status === 401) {
    return;
  }

  // Show error toast with more details for mutations
  toast.error(apiError.message || 'Operation failed', {
    description: apiError.errors
      ? Object.entries(apiError.errors)
          .map(([field, messages]) => `${field}: ${messages.join(', ')}`)
          .join('\n')
      : undefined,
    duration: 5000,
  });
};

// Create query client
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Default query options
      staleTime: 5 * 60 * 1000, // 5 minutes
      gcTime: 10 * 60 * 1000, // 10 minutes (formerly cacheTime)
      retry: (failureCount, error) => {
        const apiError = error as ApiError;
        
        // Don't retry on 4xx errors except 429 (Too Many Requests)
        if (apiError.status >= 400 && apiError.status < 500 && apiError.status !== 429) {
          return false;
        }
        
        // Retry up to 3 times for other errors
        return failureCount < 3;
      },
      retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 30000),
      refetchOnWindowFocus: false,
      refetchOnReconnect: true,
      refetchOnMount: true,
    },
    mutations: {
      // Default mutation options
      retry: false, // Don't retry mutations by default
      onError: handleMutationError,
    },
  },
  queryCache: new QueryCache({
    onError: handleQueryError,
  }),
  mutationCache: new MutationCache({
    onError: handleMutationError,
  }),
});

// Query keys factory for consistent key management
export const queryKeys = {
  // Auth
  auth: {
    user: ['auth', 'user'] as const,
    profile: ['auth', 'profile'] as const,
  },
  
  // Users
  users: {
    all: ['users'] as const,
    list: (params?: Record<string, any>) => ['users', 'list', params] as const,
    detail: (id: string) => ['users', 'detail', id] as const,
    stats: (id: string) => ['users', 'stats', id] as const,
    search: (term: string, params?: Record<string, any>) => 
      ['users', 'search', term, params] as const,
  },
  
  // Courses
  courses: {
    all: ['courses'] as const,
    list: (params?: Record<string, any>) => ['courses', 'list', params] as const,
    detail: (slug: string) => ['courses', 'detail', slug] as const,
    stats: (id: string) => ['courses', 'stats', id] as const,
    byInstructor: (instructorId: string) => 
      ['courses', 'instructor', instructorId] as const,
    enrolled: (userId: string) => ['courses', 'enrolled', userId] as const,
  },
  
  // Lessons
  lessons: {
    all: ['lessons'] as const,
    list: (courseId: string) => ['lessons', 'list', courseId] as const,
    detail: (id: string) => ['lessons', 'detail', id] as const,
    progress: (lessonId: string, userId: string) => 
      ['lessons', 'progress', lessonId, userId] as const,
  },
  
  // Enrollments
  enrollments: {
    all: ['enrollments'] as const,
    list: (params?: Record<string, any>) => ['enrollments', 'list', params] as const,
    detail: (id: string) => ['enrollments', 'detail', id] as const,
    byCourse: (courseId: string) => ['enrollments', 'course', courseId] as const,
    byStudent: (studentId: string) => ['enrollments', 'student', studentId] as const,
  },
  
  // Assignments
  assignments: {
    all: ['assignments'] as const,
    list: (lessonId: string) => ['assignments', 'list', lessonId] as const,
    detail: (id: string) => ['assignments', 'detail', id] as const,
    submissions: (assignmentId: string, userId: string) => 
      ['assignments', 'submissions', assignmentId, userId] as const,
  },
  
  // Categories
  categories: {
    all: ['categories'] as const,
    list: () => ['categories', 'list'] as const,
    detail: (id: string) => ['categories', 'detail', id] as const,
    tree: () => ['categories', 'tree'] as const,
  },
  
  // Tags
  tags: {
    all: ['tags'] as const,
    list: () => ['tags', 'list'] as const,
    popular: () => ['tags', 'popular'] as const,
  },
  
  // Notifications
  notifications: {
    all: ['notifications'] as const,
    list: (params?: Record<string, any>) => ['notifications', 'list', params] as const,
    unreadCount: () => ['notifications', 'unread-count'] as const,
  },
};