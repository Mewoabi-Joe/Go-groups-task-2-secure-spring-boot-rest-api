package com.example.test1.controllers;

import com.example.test1.models.Category;
import com.example.test1.responses.StatisticsResponse;
import com.example.test1.responses.TemplatesByCategoryResponse;
import com.example.test1.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping  //func 1
    public ResponseEntity<List<TemplatesByCategoryResponse>> getAllCategoryInfoIncludingTemplates(){
//        System.out.println("IsAuthenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
//               System.out.println("Details: " + SecurityContextHolder.getContext().getAuthentication().getDetails().toString());
//        System.out.println("Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println("Name: " + SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(categoryService.findAll(),HttpStatus.OK);
    }

    @PostMapping //funt
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        System.out.println(category.toString());
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);

    }

    @GetMapping("/statistics")
    public ResponseEntity<List<StatisticsResponse>> getCategoryStatistics(){
        return new ResponseEntity<>(categoryService.getCategoryStatistics(), HttpStatus.OK);
    }



}
