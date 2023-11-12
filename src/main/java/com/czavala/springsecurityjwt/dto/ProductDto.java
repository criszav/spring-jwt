package com.czavala.springsecurityjwt.dto;

import com.czavala.springsecurityjwt.persistance.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private String name;

    private BigDecimal price;

    private Status status;

    private String category;
}
