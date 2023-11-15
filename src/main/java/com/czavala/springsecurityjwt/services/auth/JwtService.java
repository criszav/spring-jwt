package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.persistance.entities.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
        Date expirationDate = new Date((EXPIRATION_IN_MINUTES * 1000 * 60) + issuedAt.getTime());

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
}
