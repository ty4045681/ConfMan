package com.finale.ConferenceManagement.configuration;

import com.finale.ConferenceManagement.model.UserRole;
import com.finale.ConferenceManagement.repository.UserRepository;
import com.finale.ConferenceManagement.security.JwtAuthenticationFilter;
import com.finale.ConferenceManagement.security.UserDetailsServiceImpl;
import com.finale.ConferenceManagement.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class AppSecurityConfig {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AppSecurityConfig(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/", "/index.html").permitAll()
                            .requestMatchers("/api/auth/**", "/api/conference/**", "/api/paper/**", "/api/presentation/**","/error", "/actuator/**").permitAll()
                            .requestMatchers("/api/user/register", "/api/user/check-email", "/api/user/check-username").permitAll()
                            .requestMatchers("/api/user/**").permitAll()
                            .requestMatchers("/api/admin/**").permitAll()
                            .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout().permitAll();
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/", "/index.html", "/images/**", "/js/**", "/webjars/**", "/api/auth/**", "/api/user/**", "/api/admin/**","/api/conference/**", "/api/paper/**", "/api/presentation/**","/error", "/actuator/**");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
