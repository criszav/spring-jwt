package com.czavala.springsecurityjwt.services.impl;

import com.czavala.springsecurityjwt.dto.CategoryDto;
import com.czavala.springsecurityjwt.dto.SaveCategoryDto;
import com.czavala.springsecurityjwt.exceptions.ObjectNotFoundException;
import com.czavala.springsecurityjwt.persistance.entities.Category;
import com.czavala.springsecurityjwt.persistance.entities.Status;
import com.czavala.springsecurityjwt.persistance.repositories.CategoryRepository;
import com.czavala.springsecurityjwt.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(category -> mapEntityCategoryToDto(category));
    }

    @Override
    public Optional<CategoryDto> findOneById(Long id) {
        return categoryRepository.findById(id).map(category -> mapEntityCategoryToDto(category));
    }

    @Override
    public CategoryDto createOne(SaveCategoryDto saveCategoryDto) {
        Category category = new Category();
        category.setName(saveCategoryDto.getName());
        category.setStatus(Status.ENABLED);

        categoryRepository.save(category);
        return mapEntityCategoryToDto(category);
    }

    @Override
    public CategoryDto updateOneById(Long id, SaveCategoryDto saveCategoryDto) {
        Category categoryFromDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found. Category id: " + id));

        categoryFromDB.setName(saveCategoryDto.getName());

        categoryRepository.save(categoryFromDB);
        return mapEntityCategoryToDto(categoryFromDB);
    }

    @Override
    public CategoryDto disableOneById(Long id) {
        Category categoryFromDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found. Category id: " + id));

        categoryFromDB.setStatus(Status.DISABLED);

        categoryRepository.save(categoryFromDB);
        return mapEntityCategoryToDto(categoryFromDB);
    }

    private CategoryDto mapEntityCategoryToDto(Category category) {
        if (category == null) return null;

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setStatus(category.getStatus());

        return categoryDto;
    }
}
