package com.eduplatform.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Web MVC Configuration for EduPlatform
 * Configures web-related settings like interceptors, etc.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestLoggingInterceptor requestLoggingInterceptor;
    private final RateLimitingInterceptor rateLimitingInterceptor;

    /**
     * Configure Interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add locale change interceptor
        registry.addInterceptor(localeChangeInterceptor());
        
        // Add request logging interceptor
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/api/v1/**");
        
        // Add rate limiting interceptor
        registry.addInterceptor(rateLimitingInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/health", "/api/v1/actuator/**");
    }

    /**
     * Locale Change Interceptor Bean
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Locale Resolver Bean
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}