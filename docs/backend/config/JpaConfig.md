🔧 JpaConfig.java - Detailed Explanation
📋 Code Breakdown
Class Purpose
This configuration class sets up JPA (Java Persistence API) for your EduPlatform, handling database operations, entity scanning, auditing, and transaction management.

Annotations Explained
@Configuration

Marks this as a Spring configuration class

Spring will process this during application startup

Contains bean definitions and configuration

@EnableJpaRepositories(basePackages = "com.eduplatform.repository")

Tells Spring where to find your repository interfaces

Automatically creates implementations for repository methods

Scans com.eduplatform.repository package for repository interfaces

@EntityScan(basePackages = "com.eduplatform.model")

Tells JPA where to find your entity classes (User, Course, Enrollment, etc.)

Essential for entity discovery and mapping

Scans com.eduplatform.model package for @Entity classes

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")

Enables automatic auditing of entities

Automatically populates createdBy, updatedBy, createdAt, updatedAt fields

Uses the auditorProvider bean to get current user

@EnableTransactionManagement

Enables Spring's declarative transaction management

Allows use of @Transactional annotations

Ensures database consistency

AuditorProvider Bean
Purpose: Identifies who is making changes to entities

Returns: Current authenticated user's email or "system"

Logic:

If user is authenticated → returns username/email

If no authentication or anonymous → returns "system"

Used for audit trails in your entities

