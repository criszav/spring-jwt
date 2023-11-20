package com.czavala.springsecurityjwt.controllers;

import com.czavala.springsecurityjwt.dto.auth.AuthenticationRequestDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationResponseDto;
import com.czavala.springsecurityjwt.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
}
