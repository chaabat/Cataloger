package com.Cataloger.repository;

public class ProductRepository {

<<<<<<< HEAD
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByDesignation(String designation);
    Page<Product> findByDesignation(String designation, Pageable pageable);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
=======
>>>>>>> parent of 9d2fc01 (category , product and user done)
}
