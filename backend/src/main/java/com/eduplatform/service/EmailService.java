package com.eduplatform.service;

/**
 * Email Service Interface
 * Handles sending various types of transactional emails.
 */
public interface EmailService {

    void sendVerificationEmail(String toEmail, String firstName, String verificationToken);

    void sendWelcomeEmail(String toEmail, String firstName);

    void sendPasswordResetEmail(String toEmail, String firstName, String resetToken);

    void sendEnrollmentConfirmation(String toEmail, String firstName, String courseName);

    void sendCourseCompletionEmail(String toEmail, String firstName, String courseName, String certificateUrl);

    void sendInstructorNotification(String toEmail, String instructorName, String notificationMessage);
}
