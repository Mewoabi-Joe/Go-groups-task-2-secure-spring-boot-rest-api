package com.example.test1.controllers;

import com.example.test1.responses.ItemResponse;
import com.example.test1.responses.ItemsByCategoryResponse;
import com.example.test1.responses.StatisticsResponse;
import com.example.test1.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PutMapping("/{id}/increase")
    public ResponseEntity<ItemResponse> increaseItem(@PathVariable Long id){
        return new ResponseEntity<>(itemService.increaseItem(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/decrease")
    public ResponseEntity<ItemResponse> decreaseItem(@PathVariable Long id){
        return new ResponseEntity<>(itemService.decreaseItem(id), HttpStatus.OK);
    }

    @PostMapping("/uncategorised")
    public ResponseEntity<ItemResponse> createUncategoridedItem(
            @RequestParam(required = true) String name
    ){
        return new ResponseEntity<>(itemService.createUncategorisedItem(name), HttpStatus.OK );

    }

    @DeleteMapping("/{id}/remove_from_active_list")
    public ResponseEntity<String> removeItemFromActiveList(@PathVariable Long id){
        return new ResponseEntity<>(itemService.removeItemFromActiveList(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/change_normal_to_completed")
    public ResponseEntity<ItemResponse> changeItemStateToCompleted(@PathVariable Long id){
        return new ResponseEntity<>(itemService.changeItemStateToCompleted(id), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<StatisticsResponse>> getItemStatistics(){
        return new ResponseEntity<>(itemService.getItemStatistics(), HttpStatus.OK);
    }

}
