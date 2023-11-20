package com.czavala.springsecurityjwt.services;

import com.czavala.springsecurityjwt.dto.register.SaveUserDto;
import com.czavala.springsecurityjwt.persistance.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerOneCustomer(SaveUserDto newUser);

    Optional<User> findOneByUsername(String username);
}
