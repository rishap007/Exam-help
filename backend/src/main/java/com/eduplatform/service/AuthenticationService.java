package com.eduplatform.service;

import com.eduplatform.dto.request.LoginRequest;
import com.eduplatform.dto.response.LoginResponse;

/**
 * Authentication Service Interface
 * Handles user authentication and token management.
 */
public interface AuthenticationService {
    
    /**
     * Authenticates a user and generates access and refresh tokens.
     * @param request The login request containing email and password.
     * @return A LoginResponse with tokens and user details.
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * Generates a new access token using a valid refresh token.
     * @param refreshToken The refresh token.
     * @return A new LoginResponse with a new set of tokens.
     */
    LoginResponse refreshToken(String refreshToken);
    
    /**
     * Logs out a user by invalidating their session (e.g., deleting the refresh token).
     * @param userId The ID of the user to log out.
     */
    void logout(String userId);
    
    /**
     * Validates a JWT access token.
     * @param token The token to validate.
     * @return true if the token is valid, false otherwise.
     */
    boolean validateToken(String token);
    
    /**
     * Extracts the user ID from a JWT access token.
     * @param token The token to parse.
     * @return The user ID as a String.
     */
    String getUserIdFromToken(String token);
}
