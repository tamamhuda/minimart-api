package com.tamamhuda.minimart.config;

import com.tamamhuda.minimart.common.authorization.VerifiedUserAuthManager;
import com.tamamhuda.minimart.common.exception.AccessDeniedExceptionHandler;
import com.tamamhuda.minimart.common.exception.AuthExceptionHandler;
import com.tamamhuda.minimart.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthExceptionHandler authExceptionHandler;
    private final AuthenticationProvider authenticationProvider;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
    private final VerifiedUserAuthManager verifiedUserAuthManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // --- Additional Auth Logic ---
                        .requestMatchers("/auth/resend-verification").access((authz, context) -> {
                            Authentication authentication = authz.get();
                            User user = (User) authentication.getPrincipal();
                            if (user.isVerified()) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already verified");
                            }
                            return new AuthorizationDecision(true);
                        })

                        // --- Public Endpoints ---
                        .requestMatchers(
                                "/api-docs.yaml",
                                "/api-docs",
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",

                                "/auth/**",
                                "/healthz",
                                "/users/images/**"

                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/webhook/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/products/**",
                                "/categories/**"

                        ).permitAll()

                        // --- Authenticated Only (No Verification Required) ---
                        .requestMatchers("/healthz", "/users/me").authenticated()

                        // --- Verified User Required ---
                        .requestMatchers(
                                "/user/**",
                                "/cart/**",
                                "/orders/**",
                                "/products/**",
                                "/categories/**"
                        ).access(verifiedUserAuthManager)

                        // --- Fallback ---
                        .anyRequest().authenticated()

                )
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authExceptionHandler))
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedExceptionHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();


        configuration.setAllowedOriginPatterns(List.of( "http://localhost:3000"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }




}
