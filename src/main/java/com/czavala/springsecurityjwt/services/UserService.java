package com.czavala.springsecurityjwt.services;

import com.czavala.springsecurityjwt.dto.SaveUserDto;
import com.czavala.springsecurityjwt.persistance.entities.User;

public interface UserService {

    User registerOneCustomer(SaveUserDto newUser);
}
