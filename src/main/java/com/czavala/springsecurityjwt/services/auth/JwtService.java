package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.persistance.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    // constante que contiene la cantidad de minutos de duracion del token
    @Value("${security.jwt.expiration_in_minutes}")
    private Long EXPIRATION_IN_MINUTES;

    // clave secreta con la cual se firma el token
    @Value("${security.jwt.secret_key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis()); // fecha actual del sistema
        Date expirationDate = new Date((EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime());

        String jwt = Jwts.builder()
                // agrega claims adicionales que no son obligatorios
                .setClaims(extraClaims)
                // agregar username al jwt
                .setSubject(user.getUsername())
                // indica fecha de emision del token
                .setIssuedAt(issuedAt)
                // indica fecha de expiracion del token
                .setExpiration(expirationDate)
                // especifica tipo de token ' "typ/JWT" '
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // especifica clave de firma del token y algoritmo
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                // construye el token y retorna string equivalente al jwt
                .compact();

        return jwt;
    }

    private Key generateKey() {
        // arreglo de bytes que contiene la clave secreta
//        byte[] keyBytes = SECRET_KEY.getBytes();

        // esto lo ocupamos cuando tenemos una clave que fue codificada en base 64
        byte[] decodedKey = Decoders.BASE64.decode(SECRET_KEY);

        // genera instancia de SecretKey con algoritmo HMAC-SHA
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        // extrae todos los claims del token, y luego se obtiene el claim "subject" que contiene el username
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                // establece la clave de firma para verificar la autenticidad del token
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt)
                // obtiene el payload del token (la data)
                .getBody();
    }


    public String extractJwtFromRequest(HttpServletRequest request) {

        // 1. Obtener encabezado http "Authorization", que es quien contiene el token en el request
        String authorizationHeader = request.getHeader("Authorization");

        // 1.1 Validar que header contenga texto, es decir, que venga el token en el header
        // tambien valida que el header comience con "Bearer ", posterior a ese viene el token
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {

            // si header no tiene texto y no contiene el token, retornamos un null
            return null;
        }

        // 2. Obtiene token desde el header authorization y lo retorna
        return authorizationHeader.split(" ")[1];
    }

    public Date extractExpirationDate(String jwt) {
        return extractAllClaims(jwt).getExpiration();
    }
}
