package com.czavala.springsecurityjwt.exceptions;

import com.czavala.springsecurityjwt.dto.exceptions.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice // mapea y controla excepciones
public class GlobalExceptionHandler {

    // metodo para manejar error genericos que ocurran en peticiones
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception, HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setMessage("Ha ocurrido un error al procesar la petición. Vuelva a intentarlo.");
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        // concatena los mensajes de error de validacion que puedan existir
        apiError.setMessage("Error en la petición enviada: " +
                // obtiene los mensajes de error de validacion, en una lista
                exception.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .toList());
//                        .toList().get(0));
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
