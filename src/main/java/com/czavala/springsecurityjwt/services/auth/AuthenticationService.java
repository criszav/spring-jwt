package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.dto.RegisteredUserDto;
import com.czavala.springsecurityjwt.dto.SaveUserDto;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;

    public RegisteredUserDto registerOneCustomer(SaveUserDto newUser) {
        User user = userService.registerOneCustomer(newUser);

        RegisteredUserDto userDto = new RegisteredUserDto();
        userDto.setId(user.getId());
        userDto.setName(userDto.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().name());

        String jwt = jwtService.generateToken(user);
        userDto.setJwt(jwt);

        return userDto;
    }
}
