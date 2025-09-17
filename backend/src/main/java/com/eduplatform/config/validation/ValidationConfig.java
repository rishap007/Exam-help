// ===========================================
// VALIDATION CONFIGURATION
// ===========================================

package com.eduplatform.config.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

/**
 * Validation Configuration for EduPlatform
 * Configures Bean Validation (JSR 303) and method validation
 */
@Configuration
public class ValidationConfig {

    /**
     * Local Validator Factory Bean
     * Provides JSR 303 validation
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * Method Validation Post Processor
     * Enables validation on method parameters and return values
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }
}

// ===========================================
// CUSTOM VALIDATORS
// ===========================================

// Email Validator
package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // Initialization if needed
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return pattern.matcher(email.trim().toLowerCase()).matches();
    }
}

// Password Strength Validator
package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private int minLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireLowercase = constraintAnnotation.requireLowercase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < minLength) {
            return false;
        }

        if (requireUppercase && !Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }

        if (requireLowercase && !Pattern.compile("[a-z]").matcher(password).find()) {
            return false;
        }

        if (requireDigit && !Pattern.compile("\\d").matcher(password).find()) {
            return false;
        }

        if (requireSpecialChar && !Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find()) {
            return false;
        }

        return true;
    }
}

// Phone Number Validator
package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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

// File Extension Validator
package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidFileExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileExtensionValidator implements ConstraintValidator<ValidFileExtension, MultipartFile> {

    private List<String> allowedExtensions;

    @Override
    public void initialize(ValidFileExtension constraintAnnotation) {
        this.allowedExtensions = Arrays.asList(constraintAnnotation.extensions());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Let other validators handle empty files
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return false;
        }

        String extension = getFileExtension(originalFilename);
        return allowedExtensions.contains(extension.toLowerCase());
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}

// UUID Validator
package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidUUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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

// ===========================================
// CUSTOM VALIDATION ANNOTATIONS
// ===========================================

// Valid Email Annotation
package com.eduplatform.config.validation.annotation;

import com.eduplatform.config.validation.validator.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid email format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// Valid Password Annotation
package com.eduplatform.config.validation.annotation;

import com.eduplatform.config.validation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password does not meet security requirements";
    int minLength() default 8;
    boolean requireUppercase() default true;
    boolean requireLowercase() default true;
    boolean requireDigit() default true;
    boolean requireSpecialChar() default true;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// Valid Phone Number Annotation
package com.eduplatform.config.validation.annotation;

import com.eduplatform.config.validation.validator.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// Valid File Extension Annotation
package com.eduplatform.config.validation.annotation;

import com.eduplatform.config.validation.validator.FileExtensionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileExtension {
    String message() default "Invalid file extension";
    String[] extensions() default {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// Valid UUID Annotation
package com.eduplatform.config.validation.annotation;

import com.eduplatform.config.validation.validator.UUIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "Invalid UUID format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// ===========================================
// VALIDATION GROUPS
// ===========================================

package com.eduplatform.config.validation.group;

/**
 * Validation Groups for different operations
 */
public interface ValidationGroups {
    
    interface Create {}
    
    interface Update {}
    
    interface Delete {}
    
    interface Patch {}
    
    interface AdminOnly {}
    
    interface InstructorOnly {}
    
    interface StudentOnly {}
}