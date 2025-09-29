package com.eduplatform.service.impl;

import com.eduplatform.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${app.email.from:noreply@eduplatform.com}")
    private String fromEmail;

    @Value("${app.email.from-name:EduPlatform}")
    private String fromName;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String verificationToken) {
        log.info("Sending verification email to: {}", toEmail);
        try {
            String subject = "Please verify your email address";
            String verificationUrl = frontendUrl + "/verify-email?token=" + verificationToken;
            
            String htmlContent = loadEmailTemplate("email-verification.html")
                    .replace("{{firstName}}", firstName)
                    .replace("{{verificationUrl}}", verificationUrl);

            sendHtmlEmail(toEmail, subject, htmlContent);
            log.info("Verification email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String firstName) {
        log.info("Sending welcome email to: {}", toEmail);
        try {
            String subject = "Welcome to EduPlatform!";
            String dashboardUrl = frontendUrl + "/dashboard";
            
            String htmlContent = loadEmailTemplate("welcome-email.html")
                    .replace("{{firstName}}", firstName)
                    .replace("{{dashboardUrl}}", dashboardUrl);

            sendHtmlEmail(toEmail, subject, htmlContent);
            log.info("Welcome email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String firstName, String resetToken) {
        log.info("Sending password reset email to: {}", toEmail);
        try {
            String subject = "Reset your password";
            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
            
            String htmlContent = loadEmailTemplate("password-reset.html")
                    .replace("{{firstName}}", firstName)
                    .replace("{{resetUrl}}", resetUrl);

            sendHtmlEmail(toEmail, subject, htmlContent);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public void sendEnrollmentConfirmation(String toEmail, String firstName, String courseName) {
        log.info("Sending enrollment confirmation to: {} for course: {}", toEmail, courseName);
        try {
            String subject = "Course Enrollment Confirmation - " + courseName;
            String courseUrl = frontendUrl + "/my-courses";
            
            String htmlContent = loadEmailTemplate("enrollment-confirmation.html")
                    .replace("{{firstName}}", firstName)
                    .replace("{{courseName}}", courseName)
                    .replace("{{courseUrl}}", courseUrl);

            sendHtmlEmail(toEmail, subject, htmlContent);
            log.info("Enrollment confirmation sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send enrollment confirmation email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send enrollment confirmation email", e);
        }
    }

    @Override
    public void sendCourseCompletionEmail(String toEmail, String firstName, String courseName, String certificateUrl) {
        log.info("Sending course completion email to: {} for course: {}", toEmail, courseName);
        try {
            String subject = "Congratulations! You completed " + courseName;
            
            String htmlContent = loadEmailTemplate("course-completion.html")
                    .replace("{{firstName}}", firstName)
                    .replace("{{courseName}}", courseName)
                    .replace("{{certificateUrl}}", certificateUrl != null ? certificateUrl : "#");

            sendHtmlEmail(toEmail, subject, htmlContent);
            log.info("Course completion email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send course completion email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send course completion email", e);
        }
    }
    
    @Override
    public void sendInstructorNotification(String toEmail, String instructorName, String notificationMessage) {
        log.info("Sending instructor notification email to: {}", toEmail);
        try {
            String subject = "EduPlatform - Instructor Notification";
            
            String htmlContent = loadEmailTemplate("instructor-notification.html")
                    .replace("{{instructorName}}", instructorName)
                    .replace("{{notificationMessage}}", notificationMessage);

            sendHtmlEmail(toEmail, subject, htmlContent);
            log.info("Instructor notification email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send instructor notification email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send instructor notification email", e);
        }
    }
    
    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        try {
            helper.setFrom(fromEmail, fromName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error setting from email address", e);
        }

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    private String loadEmailTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email/" + templateName);
            byte[] binaryData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(binaryData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("Email template {} not found, using fallback.", templateName);
            return getFallbackTemplate(templateName);
        }
    }

    private String getFallbackTemplate(String templateName) {
        switch (templateName) {
            case "email-verification.html":
                return "<html><body><h1>Verify Your Email</h1><p>Hello {{firstName}},</p><p>Please click <a href='{{verificationUrl}}'>here</a> to verify.</p></body></html>";
            case "welcome-email.html":
                return "<html><body><h1>Welcome, {{firstName}}!</h1><p>Welcome to our platform! <a href='{{dashboardUrl}}'>Start learning</a>.</p></body></html>";
            case "password-reset.html":
                return "<html><body><h1>Reset Your Password</h1><p>Hello {{firstName}},</p><p>Click <a href='{{resetUrl}}'>here</a> to reset your password.</p></body></html>";
            default:
                return "<html><body><p>Email from EduPlatform</p></body></html>";
        }
    }
}
