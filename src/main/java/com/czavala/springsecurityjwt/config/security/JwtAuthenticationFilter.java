package com.czavala.springsecurityjwt.config.security;

import com.czavala.springsecurityjwt.exceptions.ObjectNotFoundException;
import com.czavala.springsecurityjwt.persistance.entities.JwtToken;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.persistance.repositories.JwtTokenRepository;
import com.czavala.springsecurityjwt.services.UserService;
import com.czavala.springsecurityjwt.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Obtener authorization header y extraer el token

        String jwt = jwtService.extractJwtFromRequest(request);

        if (jwt == null || !StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1.1 Obtener token valido y NO expirado desde DB

        Optional<JwtToken> jwtToken = jwtTokenRepository.findByToken(jwt); // buscamos en DB el token extraido desde el header authorization

        boolean isValid = validateToken(jwtToken); // validamos que el token obtenido sea valido

        // si el token no es valido, contuamos con la cadena de filtros
        if (!isValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Obtener username (subject) desde el token, al hacer eso automaticamente se valida al formato del token, su firma y fecha de expiracion
        String username = jwtService.extractUsername(jwt);

        // 3. Definir el objeto "Authentication" dentro del Security Context Holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        // implementacion de la interfaz "Authentication"
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, // nombre de usuario extraido del token
                null, // se proporciona "null" para la contrasena (credentials) porque mecanismo de autorizacion es en token y no en contrasenas
                user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetails(request)); // obtiene detalles de la peticion request
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Ejecutar resto de filtros
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(Optional<JwtToken> jwtTokenOpt) {

        if (!jwtTokenOpt.isPresent()) {
            System.out.println("Metodo 'validateToken' dice: Token no existe o no fue generado.");
            return false;
        }

        JwtToken jwtToken = jwtTokenOpt.get();

        // obtenemo fecha actual del sistema para validar la expiracion del token
        Date currentDate = new Date(System.currentTimeMillis());

        // verifica si token es valido
        // si la fecha de expiracion del token es posterior a la fecha actual, significa que el token aun es valido
        boolean isValid = jwtToken.isValid() && jwtToken.getExpirationDate().after(currentDate);

        // realiza la invalidacion del token (lo setea a false)
        if (!isValid) {
            updateTokenStatus(jwtToken);
        }

        return isValid;
    }

    private void updateTokenStatus(JwtToken jwtToken) {
        jwtToken.setValid(false); // hace que el token sea invalido
        jwtTokenRepository.save(jwtToken); // guarda en DB los cambios realizados al token
    }
}
