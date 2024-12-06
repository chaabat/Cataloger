package com.Cataloger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Cataloger.entity.Product;
import com.Cataloger.entity.Category;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByDesignation(String designation);
    Page<Product> findByDesignation(String designation, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
