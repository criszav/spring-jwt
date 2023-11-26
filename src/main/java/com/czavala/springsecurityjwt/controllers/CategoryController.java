package com.czavala.springsecurityjwt.controllers;

import com.czavala.springsecurityjwt.dto.CategoryDto;
import com.czavala.springsecurityjwt.dto.SaveCategoryDto;
import com.czavala.springsecurityjwt.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> findAll(Pageable pageable) {
        Page<CategoryDto> categories = categoryService.findAll(pageable);

        if (categories.hasContent()) {
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findOneById(@PathVariable Long id) {
        Optional<CategoryDto> categoryDto = categoryService.findOneById(id);

        if (categoryDto.isPresent()) {
            return ResponseEntity.ok(categoryDto.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createOne(@RequestBody @Valid SaveCategoryDto saveCategoryDto) {
        CategoryDto categoryDto = categoryService.createOne(saveCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateOneById(@PathVariable Long id, @RequestBody @Valid SaveCategoryDto saveCategoryDto) {
        CategoryDto categoryDto = categoryService.updateOneById(id, saveCategoryDto);
        return ResponseEntity.ok(categoryDto);
    }

    @PutMapping("/{id}/disabled")
    public ResponseEntity<CategoryDto> disableOneById(@PathVariable Long id) {
        CategoryDto categoryDto = categoryService.disableOneById(id);
        return ResponseEntity.ok(categoryDto);
    }
}
