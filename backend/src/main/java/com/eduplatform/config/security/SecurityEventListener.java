package com.eduplatform.config.security;

// import com.eduplatform.model.User;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Security Event Listener
 * Listens to security events and performs audit logging
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityEventListener {

    private final UserRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 30;

    /**
     * Handle successful authentication
     */
    @EventListener
    @Transactional
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        log.info("Authentication successful for user: {}", SecurityUtils.maskEmail(username));

        // Reset failed login attempts on successful login
        userRepository.findByEmailIgnoreCase(username).ifPresent(user -> {
            if (user.getFailedLoginAttempts() > 0) {
                user.setFailedLoginAttempts(0);
                user.setAccountLockedUntil(null);
                userRepository.save(user);
            }
        });
    }

    /**
     * Handle authentication failure
     */
    @EventListener
    @Transactional
    public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        String reason = event.getException().getMessage();
        
        log.warn("Authentication failed for user: {} - Reason: {}", 
                SecurityUtils.maskEmail(username), reason);

        // Increment failed login attempts
        userRepository.findByEmailIgnoreCase(username).ifPresent(user -> {
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedAttempts);

            // Lock account if max attempts exceeded
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
                log.warn("Account locked for user: {} due to {} failed login attempts", 
                        SecurityUtils.maskEmail(username), failedAttempts);
            }

            userRepository.save(user);
        });
    }

    /**
     * Handle logout success
     */
    @EventListener
    public void handleLogoutSuccess(LogoutSuccessEvent event) {
        String username = event.getAuthentication().getName();
        log.info("Logout successful for user: {}", SecurityUtils.maskEmail(username));
    }
}