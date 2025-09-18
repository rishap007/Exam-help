package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final String PHONE_PATTERN =
            "^[+]?[1-9]\\d{1,14}$"; // E.164 format

    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // Initialization if needed
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return true; // Let @NotNull handle null validation
        }

        // Remove spaces and dashes for validation
        String cleanPhone = phoneNumber.replaceAll("[\\s-()]", "");
        return pattern.matcher(cleanPhone).matches();
    }
}