package com.eduplatform.service;

import com.eduplatform.dto.response.EnrollmentDto;
import com.eduplatform.dto.response.EnrollmentStatsDto;
// TODO: Create the EnrollmentStatsDto class in the dto/response package
// import com.eduplatform.dto.response.EnrollmentStatsDto;
import com.eduplatform.model.Enrollment;
import com.eduplatform.model.enums.EnrollmentStatus;
import com.eduplatform.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Enrollment Service Interface
 * Defines the business logic for course enrollments.
 */
public interface EnrollmentService extends BaseService<Enrollment, EnrollmentDto> {

    EnrollmentDto enrollStudent(UUID studentId, UUID courseId);

    Optional<EnrollmentDto> findByStudentAndCourse(UUID studentId, UUID courseId);

    Page<EnrollmentDto> findByStudent(UUID studentId, Pageable pageable);

    Page<EnrollmentDto> findByCourse(UUID courseId, Pageable pageable);

    Page<EnrollmentDto> findByStatus(EnrollmentStatus status, Pageable pageable);

    boolean isStudentEnrolled(UUID studentId, UUID courseId);

    void unenrollStudent(UUID studentId, UUID courseId);

    EnrollmentDto updateStatus(UUID enrollmentId, EnrollmentStatus status);

    // EnrollmentStatsDto getEnrollmentStatistics(UUID courseId);

    List<EnrollmentDto> getActiveEnrollments(UUID studentId);

    List<EnrollmentDto> getCompletedEnrollments(UUID studentId);

    EnrollmentStatsDto getEnrollmentStatistics(UUID courseId);
}
