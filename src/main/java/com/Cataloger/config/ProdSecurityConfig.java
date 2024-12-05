package com.Cataloger.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Profile("prod")
public class ProdSecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(ProdSecurityConfig.class);
    
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain for production environment");
        
        return http
            .csrf(csrf -> {
                csrf.disable();
                log.debug("CSRF protection disabled");
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                      .maximumSessions(1)
                      .maxSessionsPreventsLogin(false);
                log.debug("Session management configured with ALWAYS policy");
            })
            .securityContext(context -> {
                context.requireExplicitSave(false);
                log.debug("Security context configured to persist automatically");
            })
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                    .anyRequest().authenticated();
                log.debug("Request authorization patterns configured");
            })
            .exceptionHandling(exception -> {
                exception
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.warn("Access denied for request to {}: {}", request.getRequestURI(), accessDeniedException.getMessage());
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.getWriter().write("Access denied: Insufficient privileges");
                    })
                    .authenticationEntryPoint((request, response, authException) -> {
                        log.warn("Authentication required for request to {}: {}", request.getRequestURI(), authException.getMessage());
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("Authentication required");
                    });
                log.debug("Exception handling configured");
            })
            .headers(headers -> {
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                log.debug("Frame options disabled");
            })
            .formLogin(form -> {
                form.disable();
                log.debug("Form login disabled");
            })
            .httpBasic(basic -> {
                basic.disable();
                log.debug("HTTP Basic authentication disabled");
            })
            .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        log.info("Initializing UserDetailsManager with JDBC authentication");
        
        // Validate database connection
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.info("Database connection validated successfully");
        } catch (Exception e) {
            log.error("Failed to validate database connection", e);
            throw new RuntimeException("Database connection validation failed", e);
        }

        var manager = new JdbcUserDetailsManager(dataSource) {
            @Override
            public boolean userExists(String username) {
                try {
                    return getJdbcTemplate().queryForObject(
                        "select count(*) from users where login = ?", 
                        Integer.class, 
                        username) == 1;
                } catch (Exception e) {
                    log.error("Error checking user existence: {}", e.getMessage());
                    return false;
                }
            }
        };
        
        // Configure user query
        String userQuery = "select login as username, password, active as enabled from users where login=? and active=true";
        manager.setUsersByUsernameQuery(userQuery);
        log.debug("Configured users query: {}", userQuery);

        // Configure authorities query
        String authoritiesQuery = "select login as username, role as authority from users where login=?";
        manager.setAuthoritiesByUsernameQuery(authoritiesQuery);
        log.debug("Configured authorities query: {}", authoritiesQuery);

        return manager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        log.info("Configuring DaoAuthenticationProvider");
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsManager());
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        
        // Add custom authentication checks
        provider.setPreAuthenticationChecks(userDetails -> {
            if (!userDetails.isEnabled()) {
                log.warn("Authentication attempt for disabled user: {}", userDetails.getUsername());
                throw new DisabledException("User account is disabled");
            }
            log.debug("Pre-authentication checks passed for user: {}", userDetails.getUsername());
        });
        
        provider.setPostAuthenticationChecks(userDetails -> {
            if (userDetails.getAuthorities().isEmpty()) {
                log.warn("User {} has no authorities assigned", userDetails.getUsername());
                throw new BadCredentialsException("User has no roles assigned");
            }
            log.debug("User {} has authorities: {}", userDetails.getUsername(), 
                     userDetails.getAuthorities());
            log.debug("Post-authentication checks passed for user: {}", userDetails.getUsername());
        });

        log.debug("DaoAuthenticationProvider configured successfully");
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        log.info("Creating AuthenticationManager");
        var manager = new ProviderManager(authenticationProvider());
        
        manager.setAuthenticationEventPublisher(new AuthenticationEventPublisher() {
            @Override
            public void publishAuthenticationSuccess(Authentication authentication) {
                log.info("Authentication successful for user '{}'", authentication.getName());
            }

            @Override
            public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
                if (exception instanceof UsernameNotFoundException) {
                    log.warn("Authentication failed: user not found - {}", exception.getMessage());
                } else if (exception instanceof BadCredentialsException) {
                    log.warn("Authentication failed: bad credentials for user '{}'", 
                            authentication.getName());
                } else {
                    log.error("Authentication failed with unexpected error", exception);
                }
            }
        });
        
        return manager;
    }
}
