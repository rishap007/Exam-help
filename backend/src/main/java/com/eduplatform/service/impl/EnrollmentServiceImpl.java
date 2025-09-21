package com.eduplatform.service.impl;

import com.eduplatform.dto.response.EnrollmentDto;
import com.eduplatform.dto.response.EnrollmentStatsDto;
import com.eduplatform.exception.BusinessLogicException;
import com.eduplatform.exception.DuplicateResourceException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.mapper.EnrollmentMapper;
import com.eduplatform.model.Course;
import com.eduplatform.model.Enrollment;
import com.eduplatform.model.User;
import com.eduplatform.model.enums.EnrollmentStatus;
import com.eduplatform.repository.CourseRepository;
import com.eduplatform.repository.EnrollmentRepository;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.service.EmailService;
import com.eduplatform.service.EnrollmentService;
import com.eduplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Override
    public EnrollmentDto enrollStudent(UUID studentId, UUID courseId) {
        log.info("Enrolling student: {} in course: {}", studentId, courseId);

        User student = findUserById(studentId);
        Course course = findCourseById(courseId);

        if (!course.isPublished()) {
            throw new BusinessLogicException("Cannot enroll in an unpublished course");
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }

        // Check course capacity
        if (course.getMaxStudents() != null) {
            long currentEnrollments = enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.ACTIVE);
            if (currentEnrollments >= course.getMaxStudents()) {
                throw new BusinessLogicException("Course has reached maximum capacity");
            }
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .enrolledAt(LocalDateTime.now())
                .progressPercentage(BigDecimal.ZERO)
                .build();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Send notifications
        emailService.sendEnrollmentConfirmation(student.getEmail(), student.getFirstName(), course.getTitle());
        notificationService.sendEnrollmentNotification(studentId, courseId);

        log.info("Student {} enrolled successfully in course {}", studentId, courseId);
        return enrollmentMapper.toDto(savedEnrollment);
    }

    @Override
    public void unenrollStudent(UUID studentId, UUID courseId) {
        log.info("Unenrolling student: {} from course: {}", studentId, courseId);
        Enrollment enrollment = findEnrollmentEntity(studentId, courseId);
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
        log.info("Student {} unenrolled successfully from course {}", studentId, courseId);
    }

    // --- FIX: ADDED ALL MISSING METHOD IMPLEMENTATIONS BELOW ---

    @Override
    public EnrollmentDto updateStatus(UUID enrollmentId, EnrollmentStatus status) {
        log.info("Updating enrollment {} to status {}", enrollmentId, status);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        
        enrollment.setStatus(status);
        if (status == EnrollmentStatus.COMPLETED) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(updatedEnrollment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<EnrollmentDto> findByStudentAndCourse(UUID studentId, UUID courseId) {
        User student = findUserById(studentId);
        Course course = findCourseById(courseId);
        return enrollmentRepository.findByStudentAndCourse(student, course)
                .map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> findByStatus(EnrollmentStatus status, Pageable pageable) {
        return enrollmentRepository.findByStatus(status, pageable)
                .map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getActiveEnrollments(UUID studentId) {
        User student = findUserById(studentId);
        return enrollmentRepository.findByStudentAndStatus(student, EnrollmentStatus.ACTIVE)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> getCompletedEnrollments(UUID studentId) {
        User student = findUserById(studentId);
        return enrollmentRepository.findByStudentAndStatus(student, EnrollmentStatus.COMPLETED)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentStatsDto getEnrollmentStatistics(UUID courseId) {
        Course course = findCourseById(courseId);
        long total = enrollmentRepository.countByCourse(course);
        long completed = enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.COMPLETED);
        
        BigDecimal completionRate = total > 0
            ? BigDecimal.valueOf(completed * 100.0 / total).setScale(2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        return EnrollmentStatsDto.builder()
                .totalEnrollments(total)
                .completedEnrollments(completed)
                .activeEnrollments(total - completed)
                .completionRate(completionRate)
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto> findAll() {
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    // --- Other methods as provided ---

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> findByStudent(UUID studentId, Pageable pageable) {
        User student = findUserById(studentId);
        return enrollmentRepository.findByStudent(student, pageable).map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> findByCourse(UUID courseId, Pageable pageable) {
        Course course = findCourseById(courseId);
        return enrollmentRepository.findByCourse(course, pageable).map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(UUID studentId, UUID courseId) {
        User student = findUserById(studentId);
        Course course = findCourseById(courseId);
        return enrollmentRepository.existsByStudentAndCourse(student, course);
    }

    @Override
    public EnrollmentDto create(EnrollmentDto dto) {
        throw new UnsupportedOperationException("Use enrollStudent() method instead");
    }

    @Override
    public EnrollmentDto update(UUID id, EnrollmentDto dto) {
        throw new UnsupportedOperationException("Use specific methods like updateStatus() instead");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnrollmentDto> findById(UUID id) {
        return enrollmentRepository.findById(id).map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> findAll(Pageable pageable) {
        return enrollmentRepository.findAll(pageable).map(enrollmentMapper::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
        enrollment.setStatus(EnrollmentStatus.DROPPED); // Soft delete
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return enrollmentRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return enrollmentRepository.count();
    }

    // --- Private Helper Methods to reduce code duplication ---

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Course findCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    private Enrollment findEnrollmentEntity(UUID studentId, UUID courseId) {
        User student = findUserById(studentId);
        Course course = findCourseById(courseId);
        return enrollmentRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for this student and course"));
    }
}