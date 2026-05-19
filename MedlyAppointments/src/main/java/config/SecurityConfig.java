package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.JwtCookieFilter;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieFilter jwtCookieFilter;

    public SecurityConfig(JwtCookieFilter jwtCookieFilter) {
        this.jwtCookieFilter = jwtCookieFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/", "/index", "/login", "/css/**", "/js/**", "/img/**").permitAll() // public resources
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptions -> exceptions
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendRedirect("/index?error=auth_required");
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.sendRedirect("/index?error=access_denied");
                                })
                );
        return http.build();
    }
}