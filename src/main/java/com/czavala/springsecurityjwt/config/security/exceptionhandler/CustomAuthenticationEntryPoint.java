package com.czavala.springsecurityjwt.config.security.exceptionhandler;

import com.czavala.springsecurityjwt.dto.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint  implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(authException.getLocalizedMessage());
        apiError.setMessage("Mensaje desde 'CustomAuthenticationEntryPoint': Acceso denegado. Debe iniciar sesión acceder a este recurso.");
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());

        // indica que el tipo de respuesta es un json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // "application/json"

        // indica http status de la respuesta
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // status 401

        // setear fecha con el modulo "jackson-datatype" (importado en el pom previamente)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonApiError = objectMapper.writeValueAsString(apiError);

        response.getWriter().write(jsonApiError);
    }
}
