package com.eduplatform.service.impl;

import com.eduplatform.dto.request.LoginRequest;
import com.eduplatform.dto.response.LoginResponse;
import com.eduplatform.dto.response.UserDto;
import com.eduplatform.exception.AuthenticationException;
import com.eduplatform.exception.ResourceNotFoundException;
import com.eduplatform.mapper.UserMapper;
import com.eduplatform.model.User;
import com.eduplatform.repository.UserRepository;
import com.eduplatform.service.AuthenticationService;
import com.eduplatform.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int ACCOUNT_LOCK_DURATION_MINUTES = 30;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        // Check if account is locked
        if (user.isAccountLocked()) {
            log.warn("Login attempt for locked account: {}", request.getEmail());
            throw new AuthenticationException("Account is temporarily locked due to multiple failed login attempts");
        }

        // Check if account is active and verified
        if (!user.isActive() || !user.isEmailVerified()) {
            log.warn("Login attempt for inactive/unverified account: {}", request.getEmail());
            throw new AuthenticationException("Account is not active or email is not verified");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            handleFailedLogin(user);
            throw new AuthenticationException("Invalid email or password");
        }

        // Reset failed attempts on success
        if (user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        storeRefreshToken(user.getId(), refreshToken);

        UserDto userDto = userMapper.toDto(user);

        log.info("Login successful for user: {}", user.getId());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenUtil.getAccessTokenExpirationTime())
                .user(userDto)
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        log.info("Token refresh attempt");

        if (!jwtTokenUtil.validateRefreshToken(refreshToken)) {
            throw new AuthenticationException("Invalid or expired refresh token");
        }

        // FIX: Changed from getUserIdFromRefreshToken to the generic getUserIdFromToken
        String userId = jwtTokenUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String storedToken = getStoredRefreshToken(user.getId());
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            log.warn("Refresh token not found in store for user: {}", user.getId());
            throw new AuthenticationException("Invalid refresh token");
        }

        if (!user.isActive()) {
            throw new AuthenticationException("User account is not active");
        }

        // Generate a new access token (refresh token rotation is optional but good practice)
        String newAccessToken = jwtTokenUtil.generateAccessToken(user);
        
        // Optional: uncomment below to rotate refresh tokens on each use
        // String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);
        // storeRefreshToken(user.getId(), newRefreshToken);

        UserDto userDto = userMapper.toDto(user);

        log.info("Token refreshed successfully for user: {}", user.getId());
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Return the original refresh token or newRefreshToken if rotating
                .tokenType("Bearer")
                .expiresIn(jwtTokenUtil.getAccessTokenExpirationTime())
                .user(userDto)
                .build();
    }

    @Override
    public void logout(String userId) {
        log.info("Logout for user: {}", userId);
        removeRefreshToken(UUID.fromString(userId));
        log.info("Logout completed for user: {}", userId);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenUtil.validateAccessToken(token);
    }

    @Override
    public String getUserIdFromToken(String token) {
        // FIX: Changed from getUserIdFromAccessToken to the generic getUserIdFromToken
        return jwtTokenUtil.getUserIdFromToken(token);
    }

    private void handleFailedLogin(User user) {
        int failedAttempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(failedAttempts);

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(ACCOUNT_LOCK_DURATION_MINUTES));
            log.warn("Account locked for user: {} due to {} failed attempts", user.getId(), failedAttempts);
        }
        userRepository.save(user);
        log.warn("Failed login attempt {} for user: {}", failedAttempts, user.getId());
    }

    private void storeRefreshToken(UUID userId, String refreshToken) {
        String key = "refresh_token:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, 7, TimeUnit.DAYS);
    }

    private String getStoredRefreshToken(UUID userId) {
        String key = "refresh_token:" + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    private void removeRefreshToken(UUID userId) {
        String key = "refresh_token:" + userId;
        redisTemplate.delete(key);
    }
}