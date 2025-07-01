package com.example.GestionUser.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
// active @PreAuthorize, @Secured, etc.
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS activé via votre configuration globale
                .cors()
                .and()
                // Désactivation CSRF pour une API REST stateless
                .csrf().disable()
                // Autorisations par URL
                .authorizeRequests()
                .antMatchers(
                        "/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html",
                        "/api/v1/auth/authenticate",
                        "/api/v1/auth/activate-account",
                        "/api/v1/auth/resend-token",
                        "/api/v1/permissions",
                        "/api/v1/permissions/distinct",
                        "/api/v1/fix-admin",
                        "/api/v1/fix-admin/**"
                )
                .permitAll()
                .anyRequest().authenticated()
                .and()
                // Pas de sessions : JWT stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Injection de votre provider custom
                .authenticationProvider(authenticationProvider)
                // Filtre JWT avant l’authentification standard
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Votre filtre d’autorisation basé sur permissions après le filtre JWT
                .addFilterAfter(new PermissionAuthorizationFilter(), JwtFilter.class)
        ;

        return http.build();
    }
}
