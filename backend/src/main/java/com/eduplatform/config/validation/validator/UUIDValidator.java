package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidUUID;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

    @Override
    public void initialize(ValidUUID constraintAnnotation) {
        // Initialization if needed
    }

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        if (uuid == null || uuid.trim().isEmpty()) {
            return true; // Let @NotNull handle null validation
        }

        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}