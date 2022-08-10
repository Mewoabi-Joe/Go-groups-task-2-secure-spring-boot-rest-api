package com.example.test1.services;

import com.example.test1.dto.TemplateDTO;
import com.example.test1.exceptions.BadRequestException;
import com.example.test1.exceptions.NotFoundException;
import com.example.test1.models.Item;
import com.example.test1.models.List;
import com.example.test1.models.Template;
import com.example.test1.repositories.ItemRepository;
import com.example.test1.responses.ItemResponse;
import com.example.test1.responses.ItemsByCategoryResponse;
import com.example.test1.responses.SimpleItemResponse;
import com.example.test1.responses.StatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    @Lazy
    private TemplateService templateService;
    @Autowired
    private ListService listService;
    @Autowired
    private UserManagementService userManagementService;

    public Item createItem(Item item){
        return itemRepository.save(item);
    }

    public ItemResponse increaseItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(!optionalItem.isPresent()){
            throw new NotFoundException("No item exist with that id");
        }else{
            Item theItem = optionalItem.get();
            if(!theItem.getList().getState().equalsIgnoreCase("active") || theItem.getTemplate().getIsDeleted() || theItem.getState().equals("deleted")){
                throw new NotFoundException("That item is not in the current active list");
            }else{
                int newQuantity = theItem.getQuantity() + 1;
                theItem.setQuantity(newQuantity);
                Item savedItem = itemRepository.save(theItem);
                return new ItemResponse(savedItem.getId(), savedItem.getTemplate().getName(), savedItem.getTemplate().getNote(), savedItem.getTemplate().getImageUrl(), savedItem.getQuantity());
            }
        }
    }

    public ItemResponse createUncategorisedItem(String itemName) {
        Template created = templateService.createandReturnTemplate(new TemplateDTO(itemName,"","http:"),"uncategorised");
        List activeList = listService.getCurrentActiveListOrCreateAndReturnIt();
        Item createdItem = createItem(new Item(created, activeList, userManagementService.getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName())));
        return new ItemResponse(createdItem.getId(),createdItem.getTemplate().getName(), createdItem.getTemplate().getNote(), createdItem.getTemplate().getImageUrl(), createdItem.getQuantity());
    }

    public String removeItemFromActiveList(Long id) {
        Item theItem = getTheItemIfItExistOrThrowError(id);
        if(listService.ItemBelongsToCurrentActiveList(id)){
            theItem.setState("deleted");
            itemRepository.save(theItem);
            return ("The Item: "+theItem.getTemplate().getName()+" has been removed from the active list");
        }else{
            throw new BadRequestException("That item is not in the active list");
        }
    }

    public Item getTheItemIfItExistOrThrowError(Long id){
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(!optionalItem.isPresent()){
            throw new BadRequestException("No item exist with that Id");
        }else{
            return optionalItem.get();
        }
    }

    public ItemResponse changeItemStateToCompleted(Long id) {
        Item theItem = getTheItemIfItExistOrThrowError(id);
        List activeList  = listService.getCurrentActiveListOrThrowError();
        java.util.List<Item> theRemain = activeList.getItems().stream().filter(item -> item.getId()== theItem.getId()).collect(Collectors.toList());
        if(theRemain.size() != 1) throw new BadRequestException("That item is not in the active list");
        if(!theRemain.get(0).getState().equals("normal")) throw new BadRequestException("That item no longer belongs to the list it was probably deleted or completed");
        theItem.setState("completed");
        Item savedItem = itemRepository.save(theItem);
        return new ItemResponse(savedItem.getId(), savedItem.getTemplate().getName(), savedItem.getTemplate().getImageUrl(),savedItem.getState(), savedItem.getQuantity());
    }

    public java.util.List<StatisticsResponse> getItemStatistics() {
        java.util.List<Item> allItems
                = userManagementService
                .getUserOrThrowError(SecurityContextHolder.getContext().getAuthentication().getName()).getItems();
        java.util.List<Item> completedItems = allItems.stream().filter(item -> item.getState().equals("completed")).collect(Collectors.toList());
        HashMap<String, Integer> map = new HashMap<>();
        completedItems.forEach(item -> {
            String item_name = item.getTemplate().getName();
            if (!map.containsKey(item_name)){
                map.put(item_name, 1);
            }else{
                int theCount = map.get(item_name);
                theCount++;
                map.put(item_name, theCount);
            }
        });
        java.util.List<StatisticsResponse> response = new java.util.ArrayList<>();
        map.keySet().forEach(key -> {
            response.add(new StatisticsResponse( key, map.get(key)));
        });
        return response;

    }

    public java.util.List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public ItemResponse decreaseItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(!optionalItem.isPresent()){
            throw new NotFoundException("No item exist with that id");
        }else{
            Item theItem = optionalItem.get();
            if(!theItem.getList().getState().equalsIgnoreCase("active") || theItem.getTemplate().getIsDeleted() || theItem.getState().equals("deleted")){
                throw new NotFoundException("That item is not in the current active list");
            }else{
                if(theItem.getQuantity() == 1) throw new BadRequestException("Atleast one unit of an item can be bought");
                int newQuantity = theItem.getQuantity() - 1;
                theItem.setQuantity(newQuantity);
                Item savedItem = itemRepository.save(theItem);
                return new ItemResponse(savedItem.getId(), savedItem.getTemplate().getName(), savedItem.getTemplate().getNote(), savedItem.getTemplate().getImageUrl(), savedItem.getQuantity());
            }
        }
    }


}
