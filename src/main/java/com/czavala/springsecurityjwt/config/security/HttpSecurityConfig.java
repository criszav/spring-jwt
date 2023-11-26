package com.czavala.springsecurityjwt.config.security;

import com.czavala.springsecurityjwt.persistance.entities.util.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class HttpSecurityConfig {

    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        SecurityFilterChain filterChain = http
                // desactiva seguridad csrf (nuestra app es stateless)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> {
                    // define una aplicacion sin estado (stateless)
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // define estrategia de autenticacion (esta inyectada)
                .authenticationProvider(authProvider)
                // ejecuta el "jwtAuthenticationFilter" antes de que se ejecute el filtro "UsernamePasswordAuthenticationFilter"
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authRequest -> {
                    requestMatchersBuilder(authRequest);
                })
                .exceptionHandling(exceptionHandler -> {
                    exceptionHandler.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionHandler.accessDeniedHandler(accessDeniedHandler);
                })
                .build();

        return filterChain;
    }


    // autorizacion basada en coincidencia de solicitudes HTTP usando permisos (authorities)
    private static void requestMatchersBuilder(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRequest) {
        // Autorizacion de endpoints de productos
        authRequest.requestMatchers(HttpMethod.GET, "/api/v1/products")
                .hasAuthority(Permission.READ_ALL_PRODUCTS.name());

        authRequest.requestMatchers(HttpMethod.GET, "/api/v1/products/{id}")
                .hasAuthority(Permission.READ_ONE_PRODUCT.name());

        authRequest.requestMatchers(HttpMethod.POST, "/api/v1/products")
                .hasAuthority(Permission.CREATE_ONE_PRODUCT.name());

        authRequest.requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}")
                .hasAuthority(Permission.UPDATE_ONE_PRODUCT.name());

        authRequest.requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}/disabled")
                .hasAuthority(Permission.DISABLE_ONE_PRODUCT.name());


        // Autorizacion endpoints de categories
        authRequest.requestMatchers(HttpMethod.GET, "/api/v1/categories")
                .hasAuthority(Permission.READ_ALL_CATEGORIES.name());

        authRequest.requestMatchers(HttpMethod.GET, "/api/v1/categories/{id}")
                .hasAuthority(Permission.READ_ONE_CATEGORY.name());

        authRequest.requestMatchers(HttpMethod.POST, "/api/v1/categories")
                .hasAuthority(Permission.CREATE_ONE_CATEGORY.name());

        authRequest.requestMatchers(HttpMethod.PUT, "/api/v1/categories/{id}")
                .hasAuthority(Permission.UPDATE_ONE_CATEGORY.name());

        authRequest.requestMatchers(HttpMethod.PUT, "/api/v1/categories/{id}/disabled")
                .hasAuthority(Permission.DISABLE_ONE_CATEGORY.name());


        // Autorizacion perfil de usuarios
        authRequest.requestMatchers(HttpMethod.GET, "/api/v1/auth/profile")
                .hasAuthority(Permission.READ_MY_PROFILE.name());


        // Autorizacion endpoints publicos: registrarse, login y validar token
        authRequest.requestMatchers(HttpMethod.POST, "/api/v1/customers/register").permitAll();
        authRequest.requestMatchers(HttpMethod.POST, "/api/v1/auth/authenticate").permitAll();
        authRequest.requestMatchers(HttpMethod.GET, "/api/v1/auth/validate").permitAll();


        // para acceder a cualquier otro endpoint del sistema, se debe estar autenticado
        authRequest.anyRequest().authenticated();
    }
}
