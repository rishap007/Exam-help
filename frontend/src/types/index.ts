// Enums matching your backend
export enum UserRole {
  STUDENT = 'STUDENT',
  INSTRUCTOR = 'INSTRUCTOR',
  ADMIN = 'ADMIN',
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  DELETED = 'DELETED',
}

export enum CourseStatus {
  DRAFT = 'DRAFT',
  PUBLISHED = 'PUBLISHED',
  ARCHIVED = 'ARCHIVED',
}

export enum CourseLevel {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED',
  EXPERT = 'EXPERT',
}

export enum LessonType {
  VIDEO = 'VIDEO',
  TEXT = 'TEXT',
  QUIZ = 'QUIZ',
  ASSIGNMENT = 'ASSIGNMENT',
}

export enum EnrollmentStatus {
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
  DROPPED = 'DROPPED',
  EXPIRED = 'EXPIRED',
}

export enum ProgressStatus {
  NOT_STARTED = 'NOT_STARTED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
}

export enum SubmissionStatus {
  SUBMITTED = 'SUBMITTED',
  GRADED = 'GRADED',
  RETURNED = 'RETURNED',
  LATE = 'LATE',
}

export enum NotificationType {
  INFO = 'INFO',
  SUCCESS = 'SUCCESS',
  WARNING = 'WARNING',
  ERROR = 'ERROR',
  COURSE_UPDATE = 'COURSE_UPDATE',
  ASSIGNMENT = 'ASSIGNMENT',
  GRADE = 'GRADE',
}

// User Types
export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  status: UserStatus;
  phoneNumber?: string;
  profilePictureUrl?: string;
  bio?: string;
  timezone: string;
  language: string;
  emailVerified: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface UserStats {
  totalCoursesEnrolled: number;
  completedCourses: number;
  inProgressCourses: number;
  totalLearningTime: number;
  averageProgress: number;
  certificatesEarned: number;
}

// Authentication Types
export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterData {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role?: UserRole;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
  expiresIn: number;
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
  expiresAt: number;
}

// Course Types
export interface Course {
  id: string;
  title: string;
  slug: string;
  shortDescription: string;
  description: string;
  thumbnailUrl?: string;
  trailerUrl?: string;
  level: CourseLevel;
  status: CourseStatus;
  price?: number;
  discountPrice?: number;
  currency: string;
  durationHours?: number;
  maxStudents?: number;
  prerequisites?: string;
  learningObjectives?: string;
  targetAudience?: string;
  language: string;
  publishedAt?: string;
  instructor: User;
  category?: Category;
  tags: Tag[];
  enrollmentCount: number;
  averageRating?: number;
  createdAt: string;
  updatedAt: string;
}

export interface CourseStats {
  totalEnrollments: number;
  activeEnrollments: number;
  completedEnrollments: number;
  averageProgress: number;
  averageCompletionTime: number;
  totalRevenue: number;
}

export interface CreateCourseRequest {
  title: string;
  shortDescription?: string;
  description?: string;
  level: CourseLevel;
  price?: number;
  discountPrice?: number;
  durationHours?: number;
  maxStudents?: number;
  prerequisites?: string;
  learningObjectives?: string;
  targetAudience?: string;
  categoryId?: string;
  tagIds?: string[];
}

// Lesson Types
export interface Lesson {
  id: string;
  title: string;
  slug: string;
  description?: string;
  type: LessonType;
  content?: string;
  videoUrl?: string;
  videoDuration?: number;
  sortOrder: number;
  isPreview: boolean;
  isMandatory: boolean;
  estimatedDuration?: number;
  courseId: string;
  createdAt: string;
  updatedAt: string;
}

export interface LessonProgress {
  id: string;
  lessonId: string;
  userId: string;
  status: ProgressStatus;
  startedAt?: string;
  completedAt?: string;
  timeSpentSeconds: number;
  videoPositionSeconds: number;
  attempts: number;
  score?: number;
}

// Enrollment Types
export interface Enrollment {
  id: string;
  studentId: string;
  courseId: string;
  status: EnrollmentStatus;
  enrolledAt: string;
  completedAt?: string;
  progressPercentage: number;
  lastAccessedAt?: string;
  certificateIssued: boolean;
  certificateUrl?: string;
  course: Course;
}

// Assignment Types
export interface Assignment {
  id: string;
  title: string;
  description?: string;
  instructions?: string;
  maxScore?: number;
  passingScore?: number;
  timeLimit?: number;
  maxAttempts?: number;
  dueDate?: string;
  isMandatory: boolean;
  lessonId: string;
}

export interface AssignmentSubmission {
  id: string;
  assignmentId: string;
  userId: string;
  submissionText?: string;
  fileUrls?: string[];
  submittedAt: string;
  status: SubmissionStatus;
  score?: number;
  feedback?: string;
  gradedAt?: string;
  gradedBy?: User;
  attemptNumber: number;
}

// Category & Tag Types
export interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  iconUrl?: string;
  color?: string;
  sortOrder?: number;
  isActive: boolean;
  parentId?: string;
}

export interface Tag {
  id: string;
  name: string;
  slug: string;
  description?: string;
  color?: string;
  usageCount: number;
}

// Notification Types
export interface Notification {
  id: string;
  userId: string;
  title: string;
  message: string;
  type: NotificationType;
  isRead: boolean;
  readAt?: string;
  actionUrl?: string;
  metadata?: Record<string, any>;
  expiresAt?: string;
  createdAt: string;
}

// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  timestamp: string;
  pagination?: PaginationMetadata;
}

export interface PaginationMetadata {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

// Error Types
export interface ApiError {
  message: string;
  status: number;
  errors?: Record<string, string[]>;
  timestamp: string;
}

// Form Types
export interface UpdateProfileRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  bio?: string;
  timezone?: string;
  language?: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}