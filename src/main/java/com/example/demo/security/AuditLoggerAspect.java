package com.example.demo.security;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggerAspect.class);

    // Bắt sự kiện đăng nhập thành công từ Spring Security
    @Before("execution(* org.springframework.security.web.authentication.AuthenticationSuccessHandler.onAuthenticationSuccess(..)) && args(request, response, authentication)")
    public void logSuccessfulLogin(HttpServletRequest request, Object response, Authentication authentication) {
        String username = authentication.getName();
        String ip = request.getRemoteAddr();
        logger.info("[AUDIT] ✅ SUCCESSFUL LOGIN - User: '{}' from IP: {}", username, ip);
    }

    // Bắt đăng xuất
    @Before("execution(* org.springframework.security.web.authentication.logout.LogoutSuccessHandler.onLogoutSuccess(..)) && args(request, response, authentication)")
    public void logLogout(HttpServletRequest request, Object response, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            String ip = request.getRemoteAddr();
            logger.info("[AUDIT] 🚪 LOGOUT - User: '{}' from IP: {}", username, ip);
        }
    }

    // Bắt mọi request vào URL /admin để giám sát
    @Before("execution(* com.example.demo.web.PageController.admin(..))")
    public void logAdminAccess() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "Anonymous";
        String ip = request.getRemoteAddr();
        
        logger.info("[AUDIT] 🛡️ ADMIN ACCESS - User: '{}' from IP: {} is accessing the Admin Dashboard.", username, ip);
    }
}
