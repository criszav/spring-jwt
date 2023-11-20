package com.czavala.springsecurityjwt.dto.register;

import com.czavala.springsecurityjwt.persistance.entities.util.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUserDto implements Serializable {


    private Long id;

    private String name;

    private String username;

    private String role;

    private String jwt;
}
