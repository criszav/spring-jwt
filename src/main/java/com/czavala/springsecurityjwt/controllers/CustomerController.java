package com.czavala.springsecurityjwt.controllers;

import com.czavala.springsecurityjwt.dto.RegisteredUserDto;
import com.czavala.springsecurityjwt.dto.SaveUserDto;
import com.czavala.springsecurityjwt.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<RegisteredUserDto> registerOneCustomer(@RequestBody @Valid SaveUserDto newUser) {
        RegisteredUserDto registeredUserDto = authenticationService.registerOneCustomer(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUserDto);
    }
}
