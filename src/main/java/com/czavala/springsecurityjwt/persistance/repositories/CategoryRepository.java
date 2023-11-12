package com.czavala.springsecurityjwt.persistance.repositories;

import com.czavala.springsecurityjwt.persistance.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
