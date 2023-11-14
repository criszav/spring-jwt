package com.czavala.springsecurityjwt.services.auth;

import com.czavala.springsecurityjwt.persistance.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String generateToken(User user) {
        return null;
    }
}
