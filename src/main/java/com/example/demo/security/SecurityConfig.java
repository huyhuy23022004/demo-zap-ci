package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // demo app => để ZAP thấy được nhiều cảnh báo, ta tắt CSRF (giải thích trong báo cáo)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/products", "/login", "/error", "/css/**", "/actuator/health", "/h2-console/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // Cứng hóa Security Headers bảo vệ ứng dụng
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())    // Chống Clickjacking
                        .xssProtection(xss -> xss.disable())  // Tắt XSS mặc định đôi khi bị cấu hình riêng
                        .contentTypeOptions(content -> content.disable()) // Để default
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager users() {
        // Tài khoản demo:
        // admin/admin123 (ROLE_ADMIN)
        // user/user123   (ROLE_USER)
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin").password("admin123").roles("ADMIN").build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user").password("user123").roles("USER").build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
