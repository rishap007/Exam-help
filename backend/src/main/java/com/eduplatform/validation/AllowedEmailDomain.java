package com.eduplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation for validating an email address belongs to an allowed domain.
 */
@Documented
@Constraint(validatedBy = AllowedEmailDomain.EmailDomainValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedEmailDomain {
    String message() default "Email domain not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowedDomains() default {};

    class EmailDomainValidator implements ConstraintValidator<AllowedEmailDomain, String> {
        
        private String[] allowedDomains;

        @Override
        public void initialize(AllowedEmailDomain constraintAnnotation) {
            this.allowedDomains = constraintAnnotation.allowedDomains();
        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (email == null || allowedDomains.length == 0) {
                return true; // No domain restrictions or null email
            }

            if (!email.contains("@")) {
                return false;
            }

            String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();
            
            for (String allowedDomain : allowedDomains) {
                if (domain.equals(allowedDomain.toLowerCase())) {
                    return true;
                }
            }
            
            return false;
        }
    }
}