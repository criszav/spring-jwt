package com.czavala.springsecurityjwt.persistance.repositories;

import com.czavala.springsecurityjwt.persistance.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
