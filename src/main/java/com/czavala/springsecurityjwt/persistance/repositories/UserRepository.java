package com.czavala.springsecurityjwt.persistance.repositories;

import com.czavala.springsecurityjwt.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
