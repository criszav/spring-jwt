package com.czavala.springsecurityjwt.controllers;

import com.czavala.springsecurityjwt.dto.ProductDto;
import com.czavala.springsecurityjwt.dto.SaveProductDto;
import com.czavala.springsecurityjwt.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(Pageable pageable) {
        Page<ProductDto> products = productService.findAll(pageable);

        if (products.hasContent()) {
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findOneById(@PathVariable Long id) {
        Optional<ProductDto> productDto = productService.findOneById(id);

        if (productDto.isPresent()) {
            return ResponseEntity.ok(productDto.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductDto> createOne(@RequestBody @Valid SaveProductDto saveProductDto) {
        ProductDto productDto = productService.createOne(saveProductDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateOneById(@PathVariable Long id, @RequestBody @Valid SaveProductDto saveProductDto) {
        ProductDto productDto = productService.updateOnyById(id, saveProductDto);
        return ResponseEntity.ok(productDto);
    }

    // en lugar de eliminar un producto, lo deshabilitamos (DISABLED)
    @PutMapping("/{id}/disabled")
    public ResponseEntity<ProductDto> disableOneById(@PathVariable Long id) {
        ProductDto productDto = productService.disableOneById(id);
        return ResponseEntity.ok(productDto);
    }
}
