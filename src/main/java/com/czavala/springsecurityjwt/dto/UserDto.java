package com.czavala.springsecurityjwt.dto;

import com.czavala.springsecurityjwt.persistance.entities.util.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private String name;

    private String username;

    private Role role;
}
