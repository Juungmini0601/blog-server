package org.logly.security.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

import org.logly.dto.response.LogoutResponse;
import org.logly.response.ApiResponse;
import org.logly.security.filter.RestApiLoginAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security, AuthenticationManager authenticationManager)
            throws Exception {
        RestApiLoginAuthenticationFilter restApiLoginAuthenticationFilter = new RestApiLoginAuthenticationFilter(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/v1/auth/login"),
                authenticationManager);

        security.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterAt(restApiLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST, "/v1/users/register", "/v1/auth/login")
                                .permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/v1/posts/**",
                                        "/v1/comments/**",
                                        "/v1/followees/**",
                                        "/v1/followers/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .logout(logout -> logout.logoutUrl("/v1/auth/logout").logoutSuccessHandler(this::logoutHandler));

        return security.build();
    }

    // TODO Value Properties,
    // 참고: https://ddonghyeo.tistory.com/56
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With", "Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void logoutHandler(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String message;

        if (authentication != null && authentication.isAuthenticated()) {
            response.setStatus(HttpStatus.OK.value());
            message = "Logout success.";
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            message = "Logout failed.";
        }

        try {
            response.getWriter()
                    .write(objectMapper.writeValueAsString(ApiResponse.success(new LogoutResponse(message))));
        } catch (IOException ex) {
            log.error("Response failed. cause: {}", ex.getMessage());
        }
    }
}
