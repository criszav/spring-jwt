package com.czavala.springsecurityjwt.services.impl;

import com.czavala.springsecurityjwt.dto.register.SaveUserDto;
import com.czavala.springsecurityjwt.exceptions.InvalidPasswordException;
import com.czavala.springsecurityjwt.persistance.entities.User;
import com.czavala.springsecurityjwt.persistance.entities.util.Role;
import com.czavala.springsecurityjwt.persistance.repositories.UserRepository;
import com.czavala.springsecurityjwt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerOneCustomer(SaveUserDto newUser) {

        // valida que las contrasenas ingresadas por el user (al registrarse) sean correctas y coincidan
        validatePassword(newUser);

        User user = new User();
        user.setName(newUser.getName());
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);

        userRepository.save(user);
        return user;
    }

    private void validatePassword(SaveUserDto newUser) {

        // verifica que campos password y repeatedPassword contengan texto
        if (!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        // verifica que campos password y repeatedPassword coincidan
        if (!newUser.getPassword().equals(newUser.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }
    }
}
