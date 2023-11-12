package com.czavala.springsecurityjwt.dto;

import com.czavala.springsecurityjwt.persistance.entities.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {

    @NotBlank
    private String name;

    private Status status;
}
