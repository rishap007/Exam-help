package com.eduplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * Annotation for validating that a password meets strong criteria.
 */
@Documented
@Constraint(validatedBy = StrongPassword.StrongPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Password must contain at least 8 characters with uppercase, lowercase, digit and special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
        
        private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        );

        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {
            if (password == null) {
                return false;
            }
            return STRONG_PASSWORD_PATTERN.matcher(password).matches();
        }
    }
}