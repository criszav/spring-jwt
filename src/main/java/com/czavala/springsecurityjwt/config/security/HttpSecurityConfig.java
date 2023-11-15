package com.czavala.springsecurityjwt.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class HttpSecurityConfig {

    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // desactiva seguridad csrf (nuestra app es stateless)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> {
                    // define una aplicacion sin estado (stateless)
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // define estrategia de autenticacion (esta inyectada)
                .authenticationProvider(authProvider)
                .authorizeHttpRequests(authrequest -> {
                    // endpoints register y auth son publicos (para registrarse y logearse)
                    authrequest.requestMatchers(HttpMethod.POST, "/api/v1/customers/register").permitAll();
                    authrequest.requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll();
                    // para acceder a cualquier otro endpoint del sistema, se debe estar autenticado
                    authrequest.anyRequest().authenticated();
                })
                .build();
    }
}
