package com.czavala.springsecurityjwt.config.security.exceptionhandler;

import com.czavala.springsecurityjwt.dto.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(accessDeniedException.getLocalizedMessage());
        apiError.setMessage("Mensaje desde 'AccessDeniedHandler': Acceso denegado. No tiene los permisos para acceder a este recurso.");
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // codigo http para peticiones no autorizadas (no tiene permisos)
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // setear fecha con el modulo "jackson-datatype" (importado en el pom previamente)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonApiError = objectMapper.writeValueAsString(apiError);

        response.getWriter().write(jsonApiError);
    }
}
