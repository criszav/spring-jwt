package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.dto.register.RegisteredUserDto;
import com.czavala.springsecurityjwt.dto.register.SaveUserDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationRequestDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationResponseDto;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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

    public AuthenticationResponseDto login(AuthenticationRequestDto authRequest) {

        // creo un token "authentication" con el username y password ingresados por el usuario que se esta logeando
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        // realiza la autenticacion como tal utilizando el token "authentication" recien creado
        authenticationManager.authenticate(authentication);

        // luego de autenticar al usuario, busca los detalles de ese usuario para enviarlos al jwtService y así crear el token jwt con sus datos (del usuario logeado)
        UserDetails user = userService.findOneByUsername(authRequest.getUsername()).get();

        // genera token jwt con los datos del usuario logeado
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));

        // creamos la respuesta al usuario con el token jwt generado, luego de haber iniciado sesion
        AuthenticationResponseDto authResponse = new AuthenticationResponseDto();
        // agrega el jwt a la respuesta
        authResponse.setJwt(jwt);

        return authResponse;
    }

    public Boolean validateToken(String jwt) {

        try {
            // extrae el username del jwt, al hacerlo, esta validando que el token sea valido:
            // verifica que el formato sea correcto, valida la firma y si el token expiró
            jwtService.extractUsername(jwt);
            // si logra extraer el username del jwt, el token es valido y retorna true
            return true;

        } catch (Exception e) {
            System.out.println("Llegó al catch del metodo validateToken en AuthenticationService: " + e.getMessage());
            // si no logra extraer el username del jwt, entonces el token no es valido y se retorna false
            return false;
        }
    }
}
