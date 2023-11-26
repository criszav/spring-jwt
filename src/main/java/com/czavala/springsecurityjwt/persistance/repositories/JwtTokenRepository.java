package com.czavala.springsecurityjwt.persistance.repositories;

import com.czavala.springsecurityjwt.persistance.entities.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String jwt);
}
