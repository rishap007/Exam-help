
package com.eduplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

/**
 * Email Service Implementation
 * Complete implementation for educational platform email communications
 */
@Slf4j
@Service
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String verificationToken) {
        try {
            String subject = "Verify Your Email Address";
            String verificationUrl = frontendUrl + "/verify-email?token=" + verificationToken;

            String content = String.format("""
                Hello %s,

                Thank you for registering with EduPlatform!

                Please click the link below to verify your email address:
                %s

                This link will expire in 24 hours.

                If you didn't create an account, please ignore this email.

                Best regards,
                EduPlatform Team
                """, firstName, verificationUrl);

            sendEmail(toEmail, subject, content);
            log.info("Verification email sent to: {} for user: {}", toEmail, firstName);
        } catch (Exception e) {
            log.error("Failed to send verification email to {} for user {}: {}", toEmail, firstName, e.getMessage(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            String subject = "Welcome to EduPlatform!";

            String content = String.format("""
                Hello %s,

                Welcome to EduPlatform! 

                Your email has been verified and your account is now active.
                You can now access all features of our learning platform.

                Get started by:
                - Exploring our course catalog
                - Setting up your profile
                - Enrolling in your first course
                - Connecting with fellow learners

                If you have any questions, feel free to contact our support team.

                Happy learning!

                Best regards,
                EduPlatform Team
                """, firstName);

            sendEmail(toEmail, subject, content);
            log.info("Welcome email sent to: {} for user: {}", toEmail, firstName);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {} for user {}: {}", toEmail, firstName, e.getMessage(), e);
            // Don't throw exception for welcome email failures
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String firstName, String resetToken) {
        try {
            String subject = "Reset Your Password";
            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;

            String content = String.format("""
                Hello %s,

                You have requested to reset your password for EduPlatform.

                Please click the link below to reset your password:
                %s

                This link will expire in 1 hour.

                If you didn't request this password reset, please ignore this email.
                Your password will remain unchanged.

                Best regards,
                EduPlatform Team
                """, firstName, resetUrl);

            sendEmail(toEmail, subject, content);
            log.info("Password reset email sent to: {} for user: {}", toEmail, firstName);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {} for user {}: {}", toEmail, firstName, e.getMessage(), e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendEnrollmentConfirmation(String toEmail, String firstName, String courseName) {
        try {
            String subject = "Course Enrollment Confirmed - " + courseName;
            String courseUrl = frontendUrl + "/courses/" + courseName.toLowerCase().replace(" ", "-");

            String content = String.format("""
                Hello %s,

                Congratulations! You have successfully enrolled in:
                📚 %s

                You now have full access to:
                - All course materials and lectures
                - Interactive assignments and quizzes
                - Discussion forums with other students
                - Direct communication with instructors

                Start your learning journey here:
                %s

                We're excited to have you on board and wish you success in your studies!

                Best regards,
                EduPlatform Team
                """, firstName, courseName, courseUrl);

            sendEmail(toEmail, subject, content);
            log.info("Enrollment confirmation email sent to: {} for course: {}", toEmail, courseName);
        } catch (Exception e) {
            log.error("Failed to send enrollment confirmation email to {} for course {}: {}", toEmail, courseName, e.getMessage(), e);
            throw new RuntimeException("Failed to send enrollment confirmation email", e);
        }
    }

    @Override
    public void sendCourseCompletionEmail(String toEmail, String firstName, String courseName, String certificateUrl) {
        try {
            String subject = "🎉 Congratulations! Course Completed - " + courseName;

            String content = String.format("""
                Hello %s,

                🎉 CONGRATULATIONS! 🎉

                You have successfully completed:
                📚 %s

                Your dedication and hard work have paid off!

                🏆 Your Certificate of Completion is ready:
                %s

                What's next?
                - Share your achievement on social media
                - Add this certification to your resume
                - Explore advanced courses in this subject
                - Leave a review to help other learners

                We're proud of your accomplishment and look forward to supporting your continued learning journey!

                Best regards,
                EduPlatform Team
                """, firstName, courseName, certificateUrl);

            sendEmail(toEmail, subject, content);
            log.info("Course completion email sent to: {} for course: {}", toEmail, courseName);
        } catch (Exception e) {
            log.error("Failed to send course completion email to {} for course {}: {}", toEmail, courseName, e.getMessage(), e);
            throw new RuntimeException("Failed to send course completion email", e);
        }
    }

    @Override
    public void sendInstructorNotification(String toEmail, String instructorName, String notificationMessage) {
        try {
            String subject = "EduPlatform - Instructor Notification";

            String content = String.format("""
                Hello %s,

                You have a new notification from EduPlatform:

                %s

                To manage your courses and students, please visit your instructor dashboard:
                %s/instructor/dashboard

                If you have any questions or need assistance, please contact our instructor support team.

                Best regards,
                EduPlatform Team
                """, instructorName, notificationMessage, frontendUrl);

            sendEmail(toEmail, subject, content);
            log.info("Instructor notification email sent to: {} ({})", toEmail, instructorName);
        } catch (Exception e) {
            log.error("Failed to send instructor notification email to {} ({}): {}", toEmail, instructorName, e.getMessage(), e);
            throw new RuntimeException("Failed to send instructor notification email", e);
        }
    }

    /**
     * Private helper method to send email using Spring Mail
     * Centralizes email sending logic and configuration
     */
    private void sendEmail(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}
