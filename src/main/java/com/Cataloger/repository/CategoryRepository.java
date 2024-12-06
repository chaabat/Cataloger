package com.Cataloger.repository;

public class CategoryRepository {

<<<<<<< HEAD
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByName(String name, Pageable pageable);
    boolean existsByName(String name);
=======
>>>>>>> parent of 9d2fc01 (category , product and user done)
}
