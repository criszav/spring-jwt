package com.czavala.springsecurityjwt.controllers;

import com.czavala.springsecurityjwt.dto.LogoutResponseDto;
import com.czavala.springsecurityjwt.dto.UserDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationRequestDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationResponseDto;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.services.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // login endpoint
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid AuthenticationRequestDto authenticationRequestDto) {
        AuthenticationResponseDto authResponse = authenticationService.login(authenticationRequestDto);
        return ResponseEntity.ok(authResponse);
    }

    // endpoint para validar el token jwt
    @GetMapping("/validate")
    private ResponseEntity<Boolean> validateToken(@RequestParam String jwt) {
        Boolean isTokenValid = authenticationService.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }

    // endpoint perfil usuario
    @GetMapping("/profile")
    private ResponseEntity<UserDto> findProfile() {
        UserDto user = authenticationService.findUserProfile();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    private ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok(new LogoutResponseDto("Logout exitoso"));
    }
}
