package com.eduplatform.config.validation.validator;

import com.eduplatform.config.validation.annotation.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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