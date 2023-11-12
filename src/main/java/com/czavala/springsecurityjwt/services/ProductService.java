package com.czavala.springsecurityjwt.services;

import com.czavala.springsecurityjwt.dto.ProductDto;
import com.czavala.springsecurityjwt.dto.SaveProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<ProductDto> findAll(Pageable pageable);

    Optional<ProductDto> findOneById(Long id);

    ProductDto createOne(SaveProductDto saveProductDto);

    ProductDto updateOnyById(Long id, SaveProductDto saveProductDto);

    ProductDto disableOneById(Long id);
}
