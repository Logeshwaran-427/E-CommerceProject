package com.project.PRODUCT.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.PRODUCT.Entity.Categories;

@Repository
public interface CategoryRepository extends JpaRepository<Categories,Long> {
    boolean existsByNameIgnoreCase(String name);
}
