package com.eduplatform.dto.request;

import com.eduplatform.config.validation.annotation.ValidEmail;
import com.eduplatform.config.validation.annotation.ValidPassword;
import com.eduplatform.config.validation.annotation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    
    @ValidEmail
    @NotBlank(message = "Email is required")
    private String email;
    
    @ValidPassword
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @ValidPhoneNumber
    private String phoneNumber;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
    private String timezone;
    private String language;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
    
    @NotNull(message = "You must accept the terms and conditions")
    @AssertTrue(message = "You must accept the terms and conditions")
    private Boolean acceptTerms;
}