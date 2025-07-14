package org.fai.study.projectsem4.configuration;

import lombok.AllArgsConstructor;
import org.fai.study.projectsem4.service.Impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SercurityConfig {
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Cấu hình để tắt CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**",
                                         "/swagger-ui.html",
                                         "/swagger-ui/**",
                                         "/v3/api-docs",
                                         "/v3/api-docs/**",
                                         "/swagger-resources/**",
                                         "/webjars/**"
                        ).permitAll() // Cho phép tất cả các yêu cầu đến /api/auth/**
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/shipper/**").hasRole("SHIPPER")
                        .anyRequest().authenticated() // Các yêu cầu khác phải được xác thực
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không lưu trữ session

                );
        http.addFilterBefore(jwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JWTAuthTokenFilter jwtAuthTokenFilter() {
        return new JWTAuthTokenFilter();
    }

}
