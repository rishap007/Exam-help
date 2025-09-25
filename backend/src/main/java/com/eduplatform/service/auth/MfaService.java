package com.eduplatform.service.auth;

// import com.eduplatform.model.User;
// import com.eduplatform.repository.UserRepository;
// import com.eduplatform.service.EmailService;
import com.eduplatform.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
// import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Multi-Factor Authentication Service
 * Handles MFA token generation and verification
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MfaService {

    private final RedisTemplate<String, String> redisTemplate;
    // private final EmailService emailService;
    // private final UserRepository userRepository;
    
    private static final String MFA_TOKEN_PREFIX = "mfa:";
    private static final int MFA_TOKEN_EXPIRY_MINUTES = 5;
    // private static final int MFA_TOKEN_LENGTH = 6;

    /**
     * Generate and send MFA token
     */
    public boolean generateAndSendMfaToken(String email) {
        try {
            // User user = userRepository.findByEmailIgnoreCase(email)
            //         .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate 6-digit code
            String mfaToken = SecurityUtils.generateVerificationCode();
            
            // Store in Redis with expiration
            String key = MFA_TOKEN_PREFIX + email;
            redisTemplate.opsForValue().set(key, mfaToken, Duration.ofMinutes(MFA_TOKEN_EXPIRY_MINUTES));
            
            // Send via email
            sendMfaTokenEmail(email, mfaToken);
            
            log.info("MFA token generated for user: {}", SecurityUtils.maskEmail(email));
            return true;
            
        } catch (Exception e) {
            log.error("Failed to generate MFA token for {}: {}", SecurityUtils.maskEmail(email), e.getMessage());
            return false;
        }
    }

    /**
     * Verify MFA token
     */
    public boolean verifyMfaToken(String email, String token) {
        try {
            String key = MFA_TOKEN_PREFIX + email;
            String storedToken = redisTemplate.opsForValue().get(key);
            
            if (storedToken != null && storedToken.equals(token)) {
                // Remove token after successful verification
                redisTemplate.delete(key);
                log.info("MFA token verified successfully for user: {}", SecurityUtils.maskEmail(email));
                return true;
            }
            
            log.warn("Invalid MFA token for user: {}", SecurityUtils.maskEmail(email));
            return false;
            
        } catch (Exception e) {
            log.error("Error verifying MFA token for {}: {}", SecurityUtils.maskEmail(email), e.getMessage());
            return false;
        }
    }

    /**
     * Check if MFA token exists for user
     */
    public boolean hasPendingMfaToken(String email) {
        String key = MFA_TOKEN_PREFIX + email;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Get remaining MFA token TTL in seconds
     */
    public long getMfaTokenTtl(String email) {
        String key = MFA_TOKEN_PREFIX + email;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null ? ttl : 0;
    }

    /**
     * Clear MFA token for user
     */
    public void clearMfaToken(String email) {
        String key = MFA_TOKEN_PREFIX + email;
        redisTemplate.delete(key);
        log.debug("MFA token cleared for user: {}", SecurityUtils.maskEmail(email));
    }

    /**
     * Send MFA token via email
     */
    private void sendMfaTokenEmail(String email, String token) {
        try {
            // String subject = "Your Verification Code";
            // String content = String.format("""
            //     Hello,
                
            //     Your verification code for EduPlatform is:
                
            //     %s
                
            //     This code will expire in %d minutes.
                
            //     If you didn't request this code, please ignore this email.
                
            //     Best regards,
            //     EduPlatform Team
            //     """, token, MFA_TOKEN_EXPIRY_MINUTES);
            
            // Use a simple mail sender here - you might want to enhance this
            // emailService.sendSimpleEmail(email, subject, content);
            
        } catch (Exception e) {
            log.error("Failed to send MFA token email to {}: {}", SecurityUtils.maskEmail(email), e.getMessage());
            throw new RuntimeException("Failed to send MFA token email", e);
        }
    }
}
