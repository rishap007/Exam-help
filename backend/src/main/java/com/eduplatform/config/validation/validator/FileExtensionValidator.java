package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidFileExtension;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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