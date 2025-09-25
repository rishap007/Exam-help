package com.eduplatform.controller;

import com.eduplatform.dto.auth.MfaDto;
import com.eduplatform.service.auth.MfaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Multi-Factor Authentication Controller
 */
@Slf4j
@RestController
@RequestMapping("/auth/mfa")
@RequiredArgsConstructor
@Tag(name = "Multi-Factor Authentication", description = "MFA operations")
@ConditionalOnProperty(name = "app.mfa.enabled", havingValue = "true", matchIfMissing = false)
public class MfaController {

    private final MfaService mfaService;

    /**
     * Send MFA token to user's email
     */
    @PostMapping("/send-token")
    @Operation(summary = "Send MFA token", description = "Send verification token to user's email")
    public ResponseEntity<MfaDto.SendTokenResponse> sendMfaToken(
            @Valid @RequestBody MfaDto.SendTokenRequest request) {
        
        log.info("MFA token request for user: {}", request.getEmail());
        
        boolean sent = mfaService.generateAndSendMfaToken(request.getEmail());
        long ttl = mfaService.getMfaTokenTtl(request.getEmail());
        
        MfaDto.SendTokenResponse response = MfaDto.SendTokenResponse.builder()
                .success(sent)
                .message(sent ? "Verification token sent to your email" : "Failed to send verification token")
                .expiresInSeconds(ttl)
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Verify MFA token
     */
    @PostMapping("/verify-token")
    @Operation(summary = "Verify MFA token", description = "Verify the MFA token sent to user's email")
    public ResponseEntity<MfaDto.VerifyTokenResponse> verifyMfaToken(
            @Valid @RequestBody MfaDto.VerifyTokenRequest request) {
        
        log.info("MFA token verification for user: {}", request.getEmail());
        
        boolean verified = mfaService.verifyMfaToken(request.getEmail(), request.getToken());
        
        MfaDto.VerifyTokenResponse response = MfaDto.VerifyTokenResponse.builder()
                .verified(verified)
                .message(verified ? "Token verified successfully" : "Invalid or expired token")
                .build();
        
        return ResponseEntity.ok(response);
    }
}