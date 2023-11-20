package com.czavala.springsecurityjwt.config.security;

import com.czavala.springsecurityjwt.exceptions.ObjectNotFoundException;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.services.UserService;
import com.czavala.springsecurityjwt.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Obtener encabezado http "Authorization", que es quien contiene el token en el request
        String authorizationHeader = request.getHeader("Authorization");

        // 1.1 Validar que header contenga texto, es decir, que venga el token en el header
        // tambien valida que el header comience con "Bearer ", posterior a ese viene el token
        if (!StringUtils.hasText(authorizationHeader) || authorizationHeader.startsWith("Bearer ")) {

            // si header no tiene texto y no contiene el token, continuamos con los filtros de la cadena
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Obtener token desde el header authorization
        String jwt = authorizationHeader.split(" ")[1];

        // 3. Obtener username (subject) desde el token, al hacer eso automaticamente se valida al formato del token, su firma y fecha de expiracion
        String username = jwtService.extractUsername(jwt);

        // 4. Definir el objeto "Authentication" dentro del Security Context Holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        // implementacion de la interfaz "Authentication"
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, // nombre de usuario extraido del token
                null, // se proporciona "null" para la contrasena (credentials) porque mecanismo de autorizacion es en token y no en contrasenas
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Ejecutar resto de filtros
        filterChain.doFilter(request, response);
    }
}
