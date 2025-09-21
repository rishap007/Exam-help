package com.eduplatform.config.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

// import jakarta.validation.Validator;

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