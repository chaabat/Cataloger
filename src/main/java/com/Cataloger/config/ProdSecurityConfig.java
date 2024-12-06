package com.Cataloger.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
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
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false))
            .securityContext(context -> context.requireExplicitSave(false))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Access denied: Insufficient privileges");
                })
                .authenticationEntryPoint((request, response, ex) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Authentication required");
                }))
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .build();
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        var manager = new JdbcUserDetailsManager(dataSource) {
            @Override
            public boolean userExists(String username) {
                try {
                    return getJdbcTemplate().queryForObject(
                        "select count(*) from users where login = ?", 
                        Integer.class, 
                        username) == 1;
                } catch (Exception e) {
                    return false;
                }
            }
        };
        
        manager.setUsersByUsernameQuery(
            "select login as username, password, active as enabled from users where login=? and active=true");
        manager.setAuthoritiesByUsernameQuery(
            "select login as username, role as authority from users where login=?");

        return manager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsManager());
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        
        provider.setPreAuthenticationChecks(userDetails -> {
            if (!userDetails.isEnabled()) {
                throw new DisabledException("User account is disabled");
            }
        });
        
        provider.setPostAuthenticationChecks(userDetails -> {
            if (userDetails.getAuthorities().isEmpty()) {
                throw new BadCredentialsException("User has no roles assigned");
            }
        });

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var manager = new ProviderManager(authenticationProvider());
        
        manager.setAuthenticationEventPublisher(new AuthenticationEventPublisher() {
            @Override
            public void publishAuthenticationSuccess(Authentication authentication) {}

            @Override
            public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {}
        });
        
        return manager;
    }
}