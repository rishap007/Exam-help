-- ===========================================
-- V1__Create_base_tables.sql
-- Initial database schema creation
-- ===========================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Create schemas
CREATE SCHEMA IF NOT EXISTS public;
CREATE SCHEMA IF NOT EXISTS audit;

-- ===========================================
-- USERS TABLE
-- ===========================================

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'STUDENT',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    phone_number VARCHAR(20),
    profile_picture_url TEXT,
    bio TEXT,
    timezone VARCHAR(50) DEFAULT 'UTC',
    language VARCHAR(10) DEFAULT 'en',
    email_verified BOOLEAN DEFAULT FALSE,
    email_verification_token VARCHAR(255),
    email_verification_expires_at TIMESTAMP WITH TIME ZONE,
    password_reset_token VARCHAR(255),
    password_reset_expires_at TIMESTAMP WITH TIME ZONE,
    last_login_at TIMESTAMP WITH TIME ZONE,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0
);

-- Users indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_email_verified ON users(email_verified);
CREATE INDEX idx_users_last_login ON users(last_login_at);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Users constraints
ALTER TABLE users ADD CONSTRAINT chk_users_role 
    CHECK (role IN ('STUDENT', 'INSTRUCTOR', 'ADMIN', 'SUPER_ADMIN'));
ALTER TABLE users ADD CONSTRAINT chk_users_status 
    CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED','PENDING_VERIFICATION', 'DELETED'));
ALTER TABLE users ADD CONSTRAINT chk_users_failed_attempts 
    CHECK (failed_login_attempts >= 0);

-- ===========================================
-- CATEGORIES TABLE
-- ===========================================

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon_url TEXT,
    color VARCHAR(7),
    sort_order INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    parent_id UUID,
    deleted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    
    CONSTRAINT fk_categories_parent FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Categories indexes
CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_categories_parent ON categories(parent_id);
CREATE INDEX idx_categories_active ON categories(is_active);
CREATE INDEX idx_categories_sort_order ON categories(sort_order);

-- ===========================================
-- COURSES TABLE
-- ===========================================

CREATE TABLE courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) UNIQUE NOT NULL,
    short_description VARCHAR(500),
    description TEXT,
    thumbnail_url TEXT,
    trailer_url TEXT,
    level VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    price DECIMAL(10,2),
    discount_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'USD',
    duration_hours INTEGER,
    max_students INTEGER,
    prerequisites TEXT,
    learning_objectives TEXT,
    target_audience TEXT,
    language VARCHAR(10) DEFAULT 'en',
    instructor_id UUID NOT NULL,
    category_id UUID,
    published_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    
    CONSTRAINT fk_courses_instructor FOREIGN KEY (instructor_id) REFERENCES users(id),
    CONSTRAINT fk_courses_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Courses indexes
CREATE INDEX idx_courses_slug ON courses(slug);
CREATE INDEX idx_courses_status ON courses(status);
CREATE INDEX idx_courses_level ON courses(level);
CREATE INDEX idx_courses_instructor ON courses(instructor_id);
CREATE INDEX idx_courses_category ON courses(category_id);
CREATE INDEX idx_courses_published_at ON courses(published_at);
CREATE INDEX idx_courses_price ON courses(price);
CREATE INDEX idx_courses_language ON courses(language);

-- Courses constraints
ALTER TABLE courses ADD CONSTRAINT chk_courses_level 
    CHECK (level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'));
ALTER TABLE courses ADD CONSTRAINT chk_courses_status 
    CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED', 'SUSPENDED'));
ALTER TABLE courses ADD CONSTRAINT chk_courses_price_positive 
    CHECK (price >= 0);
ALTER TABLE courses ADD CONSTRAINT chk_courses_discount_price_positive 
    CHECK (discount_price >= 0);

-- ===========================================
-- LESSONS TABLE
-- ===========================================

CREATE TABLE lessons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200),
    description TEXT,
    type VARCHAR(50) NOT NULL,
    content TEXT,
    video_url TEXT,
    video_duration INTEGER,
    sort_order INTEGER NOT NULL,
    is_preview BOOLEAN DEFAULT FALSE,
    is_mandatory BOOLEAN DEFAULT TRUE,
    estimated_duration INTEGER,
    course_id UUID NOT NULL,
    deleted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    
    CONSTRAINT fk_lessons_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Lessons indexes
CREATE INDEX idx_lessons_course ON lessons(course_id);
CREATE INDEX idx_lessons_type ON lessons(type);
CREATE INDEX idx_lessons_sort_order ON lessons(sort_order);
CREATE INDEX idx_lessons_slug ON lessons(slug);
CREATE INDEX idx_lessons_is_preview ON lessons(is_preview);

-- Lessons constraints
ALTER TABLE lessons ADD CONSTRAINT chk_lessons_type 
    CHECK (type IN ('VIDEO', 'TEXT', 'QUIZ', 'ASSIGNMENT', 'LIVE_SESSION', 'DOCUMENT', 'INTERACTIVE'));
ALTER TABLE lessons ADD CONSTRAINT chk_lessons_sort_order_positive 
    CHECK (sort_order > 0);
ALTER TABLE lessons ADD CONSTRAINT chk_lessons_video_duration_positive 
    CHECK (video_duration >= 0);

-- ===========================================
-- ENROLLMENTS TABLE
-- ===========================================

CREATE TABLE enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL,
    course_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    enrolled_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE,
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    last_accessed_at TIMESTAMP WITH TIME ZONE,
    certificate_issued BOOLEAN DEFAULT FALSE,
    certificate_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    
    CONSTRAINT fk_enrollments_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_enrollments_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT uk_enrollments_student_course UNIQUE (student_id, course_id)
);

-- Enrollments indexes
CREATE INDEX idx_enrollments_student ON enrollments(student_id);
CREATE INDEX idx_enrollments_course ON enrollments(course_id);
CREATE INDEX idx_enrollments_status ON enrollments(status);
CREATE INDEX idx_enrollments_enrolled_at ON enrollments(enrolled_at);
CREATE INDEX idx_enrollments_progress ON enrollments(progress_percentage);

-- Enrollments constraints
ALTER TABLE enrollments ADD CONSTRAINT chk_enrollments_status 
    CHECK (status IN ('ACTIVE', 'COMPLETED', 'DROPPED', 'SUSPENDED'));
ALTER TABLE enrollments ADD CONSTRAINT chk_enrollments_progress_range 
    CHECK (progress_percentage >= 0 AND progress_percentage <= 100);

-- ===========================================
-- USER_PROGRESS TABLE
-- ===========================================

CREATE TABLE user_progress (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    course_id UUID NOT NULL,
    enrollment_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED',
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    lessons_completed INTEGER DEFAULT 0,
    total_lessons INTEGER DEFAULT 0,
    total_time_spent INTEGER DEFAULT 0,
    last_lesson_id VARCHAR(255),
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    
    CONSTRAINT fk_user_progress_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_progress_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_user_progress_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id)
);

-- User progress indexes
CREATE INDEX idx_user_progress_user ON user_progress(user_id);
CREATE INDEX idx_user_progress_course ON user_progress(course_id);
CREATE INDEX idx_user_progress_enrollment ON user_progress(enrollment_id);
CREATE INDEX idx_user_progress_status ON user_progress(status);

-- User progress constraints
ALTER TABLE user_progress ADD CONSTRAINT chk_user_progress_status 
    CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'FAILED'));
ALTER TABLE user_progress ADD CONSTRAINT chk_user_progress_percentage_range 
    CHECK (progress_percentage >= 0 AND progress_percentage <= 100);
ALTER TABLE user_progress ADD CONSTRAINT chk_user_progress_lessons_positive 
    CHECK (lessons_completed >= 0 AND total_lessons >= 0);

-- ===========================================
-- LESSON_PROGRESS TABLE
-- ===========================================

CREATE TABLE lesson_progress (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    lesson_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED',
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    time_spent INTEGER DEFAULT 0,
    video_position INTEGER DEFAULT 0,
    attempts_count INTEGER DEFAULT 0,
    score INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 0,
    
    CONSTRAINT fk_lesson_progress_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_lesson_progress_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id),
    CONSTRAINT uk_lesson_progress_user_lesson UNIQUE (user_id, lesson_id)
);

-- Lesson progress indexes
CREATE INDEX idx_lesson_progress_user ON lesson_progress(user_id);
CREATE INDEX idx_lesson_progress_lesson ON lesson_progress(lesson_id);
CREATE INDEX idx_lesson_progress_status ON lesson_progress(status);
CREATE INDEX idx_lesson_progress_completed_at ON lesson_progress(completed_at);

-- Lesson progress constraints
ALTER TABLE lesson_progress ADD CONSTRAINT chk_lesson_progress_status 
    CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'FAILED'));
ALTER TABLE lesson_progress ADD CONSTRAINT chk_lesson_progress_time_positive 
    CHECK (time_spent >= 0);
ALTER TABLE lesson_progress ADD CONSTRAINT chk_lesson_progress_attempts_positive 
    CHECK (attempts_count >= 0);