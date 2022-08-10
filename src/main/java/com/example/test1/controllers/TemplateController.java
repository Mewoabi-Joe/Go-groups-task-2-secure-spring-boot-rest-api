package com.example.test1.controllers;

import com.example.test1.dto.TemplateDTO;
import com.example.test1.responses.*;
import com.example.test1.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping("/{categoryName}/create_template")  //func 2
    public ResponseEntity<TemplateResponse2> createTemplate(
            @RequestBody TemplateDTO template, @PathVariable String categoryName){
        System.out.println("template : " + template.toString());
        System.out.println("category name :" + categoryName);
        return new ResponseEntity<>(templateService.createTemplate(template, categoryName), HttpStatus.CREATED);
    }

    @GetMapping("/{id}") //func 4.1
    public ResponseEntity<TemplateResponse2> getTemplateById(@PathVariable Long id){
        return new ResponseEntity<>(templateService.findTemplateWithId(id),HttpStatus.OK);
    }

    @PostMapping("/{templateId}/add_to_active_list") //func 4.2
    public ResponseEntity<ItemResponse> addItemToActiveList (@PathVariable Long templateId){
        return new ResponseEntity<>(templateService.addItemToActiveList(templateId), HttpStatus.OK);

    }

    @DeleteMapping("/{templateId}/delete_template")
    public ResponseEntity<TemplateResponse> deleteTemplate(@PathVariable Long templateId){
        return new ResponseEntity<>(templateService.deleteTemplate(templateId), HttpStatus.OK);

    }

    @GetMapping("/search")
    public ResponseEntity<List<TemplatesByCategoryResponse>> searchItemTemplate(@RequestParam String searchTerm){
        return new ResponseEntity<>(templateService.searchItem(searchTerm), HttpStatus.OK);
    }
}
