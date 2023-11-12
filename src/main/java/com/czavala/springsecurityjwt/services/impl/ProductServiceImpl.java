package com.czavala.springsecurityjwt.services.impl;

import com.czavala.springsecurityjwt.dto.ProductDto;
import com.czavala.springsecurityjwt.dto.SaveProductDto;
import com.czavala.springsecurityjwt.exceptions.ObjectNotFoundException;
import com.czavala.springsecurityjwt.persistance.entities.Category;
import com.czavala.springsecurityjwt.persistance.entities.Product;
import com.czavala.springsecurityjwt.persistance.entities.Status;
import com.czavala.springsecurityjwt.persistance.repositories.ProductRepository;
import com.czavala.springsecurityjwt.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(product -> mapEntityProductToDto(product));
    }

    @Override
    public Optional<ProductDto> findOneById(Long id) {
        return productRepository.findById(id).map(product -> mapEntityProductToDto(product));
    }

    @Override
    public ProductDto createOne(SaveProductDto saveProductDto) {
        Product product = new Product();
        product.setName(saveProductDto.getName());
        product.setPrice(saveProductDto.getPrice());
        product.setStatus(Status.ENABLED);

        Category category = new Category();
        category.setId(saveProductDto.getCategoryId());

        product.setCategory(category);

        productRepository.save(product); // guarda nuevo producto en DB
        return mapEntityProductToDto(product); // mapea entity product a dto para retornarlo al cliente
    }

    @Override
    public ProductDto updateOnyById(Long id, SaveProductDto saveProductDto) {
        Product productFromDB = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found. Product id: " + id));

        productFromDB.setName(saveProductDto.getName());
        productFromDB.setPrice(saveProductDto.getPrice());

        Category category = new Category();
        category.setId(saveProductDto.getCategoryId());

        productFromDB.setCategory(category);

        productRepository.save(productFromDB); // guarda producto actualizado en DB
        return mapEntityProductToDto(productFromDB); // mapea entity product a dto para retornarlo al cliente
    }

    @Override
    public ProductDto disableOneById(Long id) {
        Product productFromDB = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found. Product id: " + id));

        productFromDB.setStatus(Status.DISABLED);

        productRepository.save(productFromDB);
        return mapEntityProductToDto(productFromDB);
    }

    private ProductDto mapEntityProductToDto(Product product) {
        if (product == null) return null;

        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStatus(product.getStatus());
        productDto.setCategory(product.getCategory().getName());

        return productDto;
    }
}
