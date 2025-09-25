package com.eduplatform.config.security.validator;

import com.eduplatform.util.SecurityUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Security Aspect for processing custom security annotations.
 */
@Aspect
@Component
public class SecurityAspect {

    @Before("@annotation(com.eduplatform.config.security.annotation.AdminOnly)")
    public void checkAdminAccess() {
        if (!SecurityUtils.hasRole("ADMIN")) {
            throw new AccessDeniedException("Admin access required");
        }
    }

    @Before("@annotation(com.eduplatform.config.security.annotation.InstructorOrAdmin)")
    public void checkInstructorOrAdminAccess() {
        if (!SecurityUtils.hasRole("ADMIN") && !SecurityUtils.hasRole("INSTRUCTOR")) {
            throw new AccessDeniedException("Instructor or Admin access required");
        }
    }
}