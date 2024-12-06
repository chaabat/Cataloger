package com.Cataloger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Cataloger.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByName(String name, Pageable pageable);
    boolean existsByName(String name);
}
