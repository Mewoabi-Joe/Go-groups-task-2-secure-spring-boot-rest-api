package com.example.test1.services;

import com.example.test1.dto.TemplateDTO;
import com.example.test1.exceptions.BadRequestException;
import com.example.test1.exceptions.NotFoundException;
import com.example.test1.models.Category;
import com.example.test1.models.Item;
import com.example.test1.models.List;
import com.example.test1.models.Template;
import com.example.test1.repositories.TemplateRepository;
import com.example.test1.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ListService listService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserManagementService userManagementService;

    public TemplateResponse2 createTemplate(TemplateDTO templateDto, String categoryName){

        Category category = categoryService.getTheExistingOrCreateAndReturnCategory(categoryName, templateDto.getName());

        Template newTemplate = new Template(templateDto.getName(),templateDto.getNote(),templateDto.getImageUrl(),
                category, userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()));
        Template savedTemplate = templateRepository.save(newTemplate);
        return new TemplateResponse2(savedTemplate.getId(), savedTemplate.getName(), savedTemplate.getNote(),
                savedTemplate.getImageUrl(),savedTemplate.getCategory().getName());
    }

    public Template createandReturnTemplate(TemplateDTO templateDto, String categoryName){

        Category category = categoryService.getTheExistingOrCreateAndReturnCategory(categoryName, templateDto.getName());

        Template newTemplate = new Template(templateDto.getName(),templateDto.getNote(),templateDto.getImageUrl(),
                category, userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()));
        Template savedTemplate = templateRepository.save(newTemplate);

        return savedTemplate;
    }

    public TemplateResponse2 findTemplateWithId(Long id){
        Optional<Template> optionalTemplate = templateRepository.findById(id);
        if(optionalTemplate.isPresent()){
            return new TemplateResponse2(optionalTemplate.get().getId(), optionalTemplate.get().getName(),optionalTemplate.get().getNote(),
                    optionalTemplate.get().getImageUrl(),optionalTemplate.get().getCategory().getName());
        }else{
            throw new NotFoundException("There is no item template with that id");
        }
    }

    public ItemResponse addItemToActiveList(Long templateId){
       Optional<Template> optionalTemplate = templateRepository.findById(templateId);
        List theList;
       if(!optionalTemplate.isPresent()){
           throw new NotFoundException("No item template exist with that Id");
       }else if(optionalTemplate.get().getIsDeleted()){
           throw new BadRequestException("No template exist with such an id, it was probably deleted");
       }else if(optionalTemplate.get().getUser().getId() != userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()).getId()){
           throw new BadRequestException("That item was not created by that user, it cannot be added to his list");
       }
       else{
           theList = listService.getCurrentActiveListOrCreateAndReturnIt();
           java.util.List<Item> matchItem = theList.getItems().stream().filter(item -> item.getTemplate().getName() == optionalTemplate.get().getName()).collect(Collectors.toList());
           if(matchItem.size() >= 1) throw new BadRequestException("There is already an item in that list with that name consider increasing its quantity");
       }
        Item createdItem =  itemService.createItem(new Item(optionalTemplate.get(), theList, userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName())));
       return new ItemResponse(createdItem.getId(), createdItem.getTemplate().getName(),
               createdItem.getTemplate().getNote(), createdItem.getTemplate().getImageUrl(), createdItem.getQuantity());

    }

    public TemplateResponse deleteTemplate(Long templateId) {
        Optional<Template> optionalTemplate = templateRepository.findById(templateId);
        if(!optionalTemplate.isPresent()){
            throw new BadRequestException("No template exist with that id");
        }else {
            optionalTemplate.get().setIsDeleted(true);
            Template modifiedTemplate = templateRepository.save(optionalTemplate.get());
            return new TemplateResponse(modifiedTemplate.getId(),modifiedTemplate.getName(),
                    modifiedTemplate.getNote(),modifiedTemplate.getImageUrl());
        }
    }

    public java.util.List<TemplatesByCategoryResponse> searchItem(String searchTerm) {
      java.util.List<Category> allCategories =
              userManagementService
                .getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()).getCategories();
        return allCategories.stream().map(category ->{
            java.util.List<TemplateResponse> templates = new ArrayList<>();
            category.getTemplates().forEach(template -> {
                if(!template.getIsDeleted() && template.getName().contains(searchTerm)) {
                    templates.add(new TemplateResponse(template.getId(), template.getName(), template.getNote(), template.getImageUrl()));
                }
            });
            return new TemplatesByCategoryResponse(category.getName(), templates );
        }).collect(Collectors.toList());
    }
}


