package com.example.test1.controllers;

import com.example.test1.responses.ActiveListResponse;
import com.example.test1.responses.ItemsByCategoryResponse;
import com.example.test1.responses.ListResponse;
import com.example.test1.services.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lists")
public class ListController {
    @Autowired
    private ListService listService;

    @GetMapping("/active")
    public ResponseEntity<ActiveListResponse> getItemsInActiveList(){
        return new ResponseEntity<>(listService.getItemsInActiveList(), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ListResponse>> getAllListHistory(){
        return new ResponseEntity<>(listService.getAllListHistory(), HttpStatus.OK);
    }

    @PatchMapping("/update_active_list_name")
    public ResponseEntity<ListResponse> updateActiveListName(
            @RequestParam String name
    ){
        return new ResponseEntity<>(listService.updateActiveListName(name), HttpStatus.OK);
    }

    @GetMapping("{id}/get_items_by_category")
    public ResponseEntity<List<ItemsByCategoryResponse>> getItemsByCategory(@PathVariable Long id){
        return new ResponseEntity<>(listService.getItemsByCategory(id), HttpStatus.OK);
    }

//    @PatchMapping("/toggle_active_edit")
//    public ResponseEntity<String> changeListFromActiveToEdit(){
//        return new ResponseEntity<>(listService.toggleListBetweenActiveAndEditState(), HttpStatus.OK);
//    }

    @PatchMapping("/change_active_to_completed")
    public ResponseEntity<String> updateListStateFromActiveToCompleted(){
        return new ResponseEntity<>(listService.updateListStateFromActiveToCompleted(), HttpStatus.OK);
    }

}
