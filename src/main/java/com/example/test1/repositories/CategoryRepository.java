package com.example.test1.repositories;

import com.example.test1.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}

//public interface CategoryRepository extends JpaRepository<Category, Long> {
//}
