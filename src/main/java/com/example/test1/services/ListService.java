package com.example.test1.services;

import com.example.test1.exceptions.BadRequestException;
import com.example.test1.models.Item;
import com.example.test1.models.List;
import com.example.test1.models.User;
import com.example.test1.repositories.ListRepository;
import com.example.test1.responses.ActiveListResponse;
import com.example.test1.responses.ItemsByCategoryResponse;
import com.example.test1.responses.ListResponse;
import com.example.test1.responses.SimpleItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ListService {
    @Autowired
    private ListRepository listRepository;
    @Autowired
    private UserManagementService userManagementService;

    public java.util.List<List> getAllList() {
      User user = userManagementService
                .getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.getLists();
    }

    public List createList(List list){
        list.setUser(userManagementService
                .getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()));
        return listRepository.save(list);
    }

    public List getCurrentActiveListOrCreateAndReturnIt(){
        List theList;
//        java.util.List<List> allLists = getAllList();
        java.util.List<List> allLists =
                userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()).getLists();
        if (allLists.size() == 0) {
            theList = createList(new List())
            ;
        } else {
            Optional<List> optionalList = allLists.stream().filter(list -> list.getState().equalsIgnoreCase("active")).findFirst();
            if (!optionalList.isPresent()) {
                List aList = new List();
                aList.setUser(userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()));
                theList = createList(aList);
            } else {
                theList = optionalList.get();
            }
        }
        return theList;
    }

    public Optional<List> getOptionalCurrentActiveList(){
        java.util.List<List> allLists = getAllList();
            Optional<List> optionalList = allLists.stream().filter(list -> list.getState().equalsIgnoreCase("active")).findFirst();
            return optionalList;
    }

    public ActiveListResponse getItemsInActiveList() {
        Optional<List> optionalActiveList = getOptionalCurrentActiveList();
        if(!optionalActiveList.isPresent()){
            return new ActiveListResponse();
        }else{
            List activeList = optionalActiveList.get();
            java.util.List<Item> items = activeList.getItems().stream().filter(item -> !item.getState().equals("deleted")).collect(Collectors.toList());
            HashMap<String, ArrayList<SimpleItemResponse>> map = new HashMap<>();
            items.forEach(item -> {
                String category_name = item.getTemplate().getCategory().getName();
                if (!map.containsKey(category_name)){
                    ArrayList<SimpleItemResponse> theArray = new ArrayList<>();
                    theArray.add( new SimpleItemResponse(item.getId(),item.getTemplate().getName(),item.getQuantity(), item.getState()));
                    map.put(category_name, theArray);
                }else{
                    ArrayList<SimpleItemResponse> theArray = map.get(category_name);
                    theArray.add( new SimpleItemResponse(item.getId(),item.getTemplate().getName(),item.getQuantity(),item.getState()));
                    map.put(category_name, theArray);
                }
            });
            java.util.List<ItemsByCategoryResponse> itemsByCategoryResponse = new java.util.ArrayList<ItemsByCategoryResponse>();
            map.keySet().forEach(key -> {
                itemsByCategoryResponse.add(new ItemsByCategoryResponse(key, map.get(key)));
            });

            return new ActiveListResponse(activeList.getId(),activeList.getName(),activeList.getState(),itemsByCategoryResponse);
        }

    }

    public Boolean ItemBelongsToCurrentActiveList(Long id){
        Optional<List> optionalActiveList = getOptionalCurrentActiveList();
        if(!optionalActiveList.isPresent()){
            return false;
        }else{
            Boolean itemBelongsToList = false;
            List activeList = optionalActiveList.get();
            for (Item item: activeList.getItems()
                 ) { if(item.getId() == id && !item.getState().equals("deleted")){
                     itemBelongsToList = true;
            };
            }
            return itemBelongsToList;
        }
    }

    public ListResponse updateActiveListName(String name) {
        Optional<List> optionalList = getOptionalCurrentActiveList();
        if(!optionalList.isPresent()){
            throw new BadRequestException("The name of an active list cannot be changed if it does not exits");
        }else{
            List activeList = optionalList.get();
            activeList.setName(name);
            List savedList = listRepository.save(activeList);
            return new ListResponse(savedList.getId(), savedList.getName(), savedList.getCreateDate(), savedList.getState());
        }
    }

    public java.util.List<ListResponse> getAllListHistory() {
        java.util.List<List> lists = getAllList();
        java.util.List<ListResponse> listResponses = new ArrayList<>();
        lists.forEach(list -> {
            listResponses.add(new ListResponse(list.getId(), list.getName(), list.getCreateDate(), list.getState()));
        });
        return listResponses;
    }

    public java.util.List<ItemsByCategoryResponse> getItemsByCategory( Long id) {
        List theList = getListWithIdOrThrowError(id);
        java.util.List<Item> items = theList.getItems();
        HashMap<String, ArrayList<SimpleItemResponse>> map = new HashMap<>();
        items.forEach(item -> {
            String category_name = item.getTemplate().getCategory().getName();
            if (!map.containsKey(category_name)){
                ArrayList<SimpleItemResponse> theArray = new ArrayList<>();
                theArray.add( new SimpleItemResponse(item.getId(),item.getTemplate().getName(),item.getQuantity(), item.getState()));
                map.put(category_name, theArray);
            }else{
                ArrayList<SimpleItemResponse> theArray = map.get(category_name);
                theArray.add( new SimpleItemResponse(item.getId(),item.getTemplate().getName(),item.getQuantity(),item.getState()));
                map.put(category_name, theArray);
            }
        });
        java.util.List<ItemsByCategoryResponse> response = new java.util.ArrayList<ItemsByCategoryResponse>();
        map.keySet().forEach(key -> {
            response.add(new ItemsByCategoryResponse(key, map.get(key)));
        });
        return response;
    }

    public List getListWithIdOrThrowError(Long id){
        Optional<List> optionalList = listRepository.findById(id);
        if(!optionalList.isPresent()){
            throw new BadRequestException("no list exist with that id");
        }else{
            return optionalList.get();
        }

    }

//    public String toggleListBetweenActiveAndEditState() {
//        List activeList = getCurrentActiveListOrThrowError();
//        if(activeList.getState().equals("active")){
//            activeList.setState("edit");
//            listRepository.save(activeList);
//            return "The state of the active list has been changed to edit";
//        }
//        if(activeList.getState().equals("edit")){
//            activeList.setState("active");
//            listRepository.save(activeList);
//            return "The state of the edit list has been changed to active";
//        }
//        throw new BadRequestException("This endpoint only toogles between active state and edit state");
//    }

    public List getCurrentActiveListOrThrowError(){
        List theList;
        java.util.List<List> allLists = getAllList();
        if (allLists.size() == 0) {
            throw new BadRequestException("There is no active list");
        } else {
            Optional<List> optionalList = allLists.stream().filter(list -> list.getState().equalsIgnoreCase("active")).findFirst();
            if (!optionalList.isPresent()) {
                throw new BadRequestException("There is no active list");
            } else {
                theList = optionalList.get();
            }
        }
        return theList;
    }

    public String updateListStateFromActiveToCompleted() {
        List activeList = getCurrentActiveListOrThrowError();
        activeList.setState("completed");
        listRepository.save(activeList);
        return "The state of the active list has been changed to completed";
    }

//    private List getCurrentEditListOrThrowError() {
//        List theList;
//        java.util.List<List> allLists = getAllList();
//        if (allLists.size() == 0) {
//            throw new BadRequestException("There is no list so We can't have it in the edit state");
//        } else {
//            Optional<List> optionalList = allLists.stream().filter(list -> list.getState().equalsIgnoreCase("edit")).findFirst();
//            if (!optionalList.isPresent()) {
//                throw new BadRequestException("There is no edit list");
//            } else {
//                theList = optionalList.get();
//            }
//        }
//        return theList;
//    }
}
