package com.czavala.springsecurityjwt.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserDto implements Serializable {

    @Size(min = 5)
    private String name;

    @Size(min = 5)
    private String username;

    @Size(min = 8) // clave debe tener al menos 8 digitos
    private String password;

    @Size(min = 8)
    private String repeatedPassword;
}
