package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.dto.UserDto;
import com.czavala.springsecurityjwt.dto.register.RegisteredUserDto;
import com.czavala.springsecurityjwt.dto.register.SaveUserDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationRequestDto;
import com.czavala.springsecurityjwt.dto.auth.AuthenticationResponseDto;
import com.czavala.springsecurityjwt.exceptions.ObjectNotFoundException;
import com.czavala.springsecurityjwt.persistance.entities.JwtToken;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.persistance.repositories.JwtTokenRepository;
import com.czavala.springsecurityjwt.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository jwtTokenRepository;

    public RegisteredUserDto registerOneCustomer(SaveUserDto newUser) {
        User user = userService.registerOneCustomer(newUser);

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        // cuando un usuario se registra,se guarda token en DB con el id del usuario al que le pertenece el token
        saveUserToken(user, jwt);

        RegisteredUserDto userDto = new RegisteredUserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().name());
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

        // crea un token "authentication" con el username y password ingresados por el usuario que se esta logeando
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        // realiza la autenticacion como tal utilizando el token "authentication" recien creado
        authenticationManager.authenticate(authentication);

        // luego de autenticar al usuario, busca los detalles de ese usuario para enviarlos al jwtService y así crear el token jwt con sus datos (del usuario logeado)
        UserDetails user = (UserDetails) userService.findOneByUsername(authRequest.getUsername()).get();

        // genera token jwt con los datos del usuario logeado
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));

        // cuando un usuario inicia sesion,se guarda token en DB con el id del usuario al que le pertenece el token
        saveUserToken((User) user, jwt);

        // creamos la respuesta al usuario con el token jwt generado, luego de haber iniciado sesion
        AuthenticationResponseDto authResponse = new AuthenticationResponseDto();
        // agrega el jwt a la respuesta
        authResponse.setJwt(jwt);

        return authResponse;
    }

    private void saveUserToken(User user, String jwt) {

        JwtToken jwtToken = new JwtToken();
        jwtToken.setUser(user);
        jwtToken.setToken(jwt);
        jwtToken.setExpirationDate(jwtService.extractExpirationDate(jwt));
        jwtToken.setValid(true);

        jwtTokenRepository.save(jwtToken);
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

    public UserDto findUserProfile() {

        // obtiene informacion del usuario actual autenticado
        Authentication auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        // obtiene el username del usuario actual desde el objeto de autenticacion
        String username = (String) auth.getPrincipal();

        // obtiene al usuario desde DB mediante el username
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        return userEntityToUserDto(user);
    }

    public void logout(HttpServletRequest request) {

        // obtiene el token jwt desde la peticion (cuando user hace logout)
        String jwt = jwtService.extractJwtFromRequest(request);

        // si token es nulo o no contiene texto, retorna el control a la cadena de filtros
        if (jwt == null || !StringUtils.hasText(jwt)) return;

        // obtiene jwt token desde db
        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);

        // verifica que token este presente y que sea un token valido
        if (token.isPresent() && token.get().isValid()) {

            // invalida el token para realizar el logout
            token.get().setValid(false);
            // guarda en la db los cambios realizados al token
            jwtTokenRepository.save(token.get());
        }
    }

    private UserDto userEntityToUserDto(User user) {
        if (user == null) return null;

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());

        return userDto;
    }
}
