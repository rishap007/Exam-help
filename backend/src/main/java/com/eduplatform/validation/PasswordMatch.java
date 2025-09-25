package com.eduplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * Class-level annotation to validate that two password fields match.
 */
@Documented
@Constraint(validatedBy = PasswordMatch.PasswordMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String password();
    String confirmPassword();

    class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
        
        private String passwordField;
        private String confirmPasswordField;

        @Override
        public void initialize(PasswordMatch constraintAnnotation) {
            this.passwordField = constraintAnnotation.password();
            this.confirmPasswordField = constraintAnnotation.confirmPassword();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext context) {
            try {
                Object password = getFieldValue(obj, passwordField);
                Object confirmPassword = getFieldValue(obj, confirmPasswordField);
                
                return password == null && confirmPassword == null || 
                       password != null && password.equals(confirmPassword);
            } catch (Exception e) {
                // Log exception if necessary
                return false;
            }
        }

        private Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        }
    }
}