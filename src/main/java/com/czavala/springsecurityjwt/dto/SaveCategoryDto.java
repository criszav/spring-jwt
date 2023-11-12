package com.czavala.springsecurityjwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveCategoryDto implements Serializable {

    @NotBlank(message = "Category name cannot be blank")
    private String name;
}
