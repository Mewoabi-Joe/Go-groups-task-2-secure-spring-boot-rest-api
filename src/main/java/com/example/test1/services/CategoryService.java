package com.example.test1.services;

import com.example.test1.exceptions.BadRequestException;
import com.example.test1.models.Category;
import com.example.test1.models.Item;
import com.example.test1.repositories.CategoryRepository;
import com.example.test1.responses.StatisticsResponse;
import com.example.test1.responses.TemplatesByCategoryResponse;
import com.example.test1.responses.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserManagementService userManagementService;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<TemplatesByCategoryResponse> findAll(){  //Find all categories alongside thier not deleted templates
//        List<Category> allCategories = categoryRepository.findAll();
        List<Category> allCategories =
                userManagementService
                        .getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName())
                        .getCategories();
        return allCategories.stream().map(category ->{
            List<TemplateResponse> templates = new ArrayList<>();
            category.getTemplates().forEach(template -> {
                if(!template.getIsDeleted()) {
                    templates.add(new TemplateResponse(template.getId(), template.getName(), template.getNote(), template.getImageUrl()));
                }
            });
            return new TemplatesByCategoryResponse(category.getName(), templates );
        }).collect(Collectors.toList());
    }

//    (List<CategoryResponse>) categoryRepository.findAll().stream().map(category ->{
//        List<ItemResponse> templates = new ArrayList<ItemResponse>();
//        category.getTemplates().forEach(template -> templates.add(template.getId(),template.getName(),template.getNote(),template.getImageUrl()));
////           re new CategoryResponse(category.getName(), new )
//    });

    public Category createCategory(Category category){
       return categoryRepository.save(category);
    }

//    public Category getTheExistingOrCreateAndReturnCategory(String categoryName){
//        Optional<Category> optionalCategory = categoryRepository.findById(categoryName);
//        if(optionalCategory.isPresent()){
//            return optionalCategory.get();
//        }else{
//            return createCategory(new Category(categoryName));
//        }
//    }


    //returns category after checking that the category does not already have a template with that name
    public Category getTheExistingOrCreateAndReturnCategory(String categoryName, String templateName){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryName);
        if(optionalCategory.isPresent()){
            optionalCategory.get().getTemplates().forEach( template -> {

                if(templateName.equals(template.getName())){
                    throw new BadRequestException("That item template name already exist in that category");
                }
            });
            return optionalCategory.get();
        }else{
            return createCategory(new Category(categoryName, userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName())));
        }
    }

    public List<StatisticsResponse> getCategoryStatistics() {
//        java.util.List<Item> allItems = itemService.getAllItems();
        java.util.List<Item> allItems =
                userManagementService
                        .getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName())
                        .getItems();
        java.util.List<Item> completedItems = allItems.stream().filter(item -> item.getState().equals("completed")).collect(Collectors.toList());
        HashMap<String, Integer> map = new HashMap<>();
        completedItems.forEach(item -> {
            String category_name = item.getTemplate().getCategory().getName();
            if (!map.containsKey(category_name)){
                map.put(category_name, 1);
            }else{
                int theCount = map.get(category_name);
                theCount++;
                map.put(category_name, theCount);
            }
        });
        java.util.List<StatisticsResponse> response = new java.util.ArrayList<>();
        map.keySet().forEach(key -> {
            response.add(new StatisticsResponse( key, map.get(key)));
        });
        return response;
    }

//    public Category findCategoryById(Long id)
}
//
//@Service
//public class CategoryService {
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    public List<Category> findAll(){
//        return categoryRepository.findAll();
//    }
//
//    public Category createCategory(CategoryDTO categoryDTO){
//        Category category = new Category();
//        category.setName(categoryDTO.getName());
//        return categoryRepository.saveAndFlush(category);
//    }
//
//    public Category findCategoryById(Long id) {
//        Optional<Category> optionalCategory = categoryRepository.findById(id);
//        if(!optionalCategory.isPresent()){
////            throw new Exception("not found");
//        }
//        return optionalCategory.get();
//    }
//}
