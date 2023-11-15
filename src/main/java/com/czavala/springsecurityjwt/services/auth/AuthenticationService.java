package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.dto.RegisteredUserDto;
import com.czavala.springsecurityjwt.dto.SaveUserDto;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;

    public RegisteredUserDto registerOneCustomer(SaveUserDto newUser) {
        User user = userService.registerOneCustomer(newUser);

        RegisteredUserDto userDto = new RegisteredUserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().name());

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        // agrega nombre del usuario a los extraClaims (info del payload del jwt) que seran agregados al token
        extraClaims.put("name", user.getName());
        // agrega nombre el role asignado del usuario a los extraClaims (info del payload del jwt) que seran agregados al token
        extraClaims.put("role", user.getRole().name());
        // agrega nombre los permisos del usuario a los extraClaims (info del payload del jwt) que seran agregados al token
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

}
