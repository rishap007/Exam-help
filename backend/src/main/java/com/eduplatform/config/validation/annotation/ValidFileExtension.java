package com.eduplatform.config.validation.annotation;

import com.eduplatform.config.validation.validator.FileExtensionValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
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