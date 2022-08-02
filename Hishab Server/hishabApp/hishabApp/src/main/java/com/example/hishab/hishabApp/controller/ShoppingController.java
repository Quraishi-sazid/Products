package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.Constants;
import com.example.hishab.hishabApp.model.Shopping;
import com.example.hishab.hishabApp.model.ShoppingItem;
import com.example.hishab.hishabApp.repository.IShoppingItemRepository;
import com.example.hishab.hishabApp.repository.IShoppingRepository;
import com.example.hishab.hishabApp.repository.IUserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping(path = "/shopping/api")
public class ShoppingController {
    @Autowired
    IShoppingRepository shoppingRepository;
    @Autowired
    IShoppingItemRepository shoppingItemRepository;
    @Autowired
    ProductController productController;
    @Autowired
    IUserRepository userRepository;

    @PostMapping("/addShopping")
    public JSONObject addShopping(@RequestBody Shopping incomingShopping) {
        JSONObject responseJson = new JSONObject();
        incomingShopping.userModel = userRepository.findUserModelByUserId(incomingShopping.userId);
        Shopping insertedShopping = shoppingRepository.save(incomingShopping);
        incomingShopping.getShoppingItemList().forEach(item -> {
            insertProductIfNeeded(insertedShopping, item, responseJson);
        });
        responseJson.put(Constants.REMOTE_SHOPPING_ID,insertedShopping.getShoppingId());
        return responseJson;
    }

    private ShoppingItem insertProductIfNeeded(Shopping insertedShopping, ShoppingItem item, JSONObject responseJSON) {
        if (item.getProduct().getProductId() == -1) {
            if (item.getCategoryName() != null && item.getCategoryName() != "") {
                item.setProduct(productController.addOrUpdateProduct(-1, -1, item.getCategoryName(), item.getProduct().getProductName(), insertedShopping.userModel.getUserId()));
            }
        }
        if (item.getProduct().getProductId() != -1) {
            item.setShopping(insertedShopping);
            var insertedItem = shoppingItemRepository.save(item);
            responseJSON.put(String.valueOf(item.localId),insertedItem.shoppingItemId);
            return insertedItem;
        }
        return null;
    }


    @PostMapping("/updateShopping")
    JSONObject updateShoppingItem(@RequestBody Shopping incomingShopping) {
        HashMap<Integer, ShoppingItem> tempHashMap = new HashMap<Integer, ShoppingItem>();
        HashSet<Integer>newInserted = new HashSet<Integer>();
        JSONObject responseJson = new JSONObject();
        List<Integer> deleteList = new ArrayList<>();
        incomingShopping.userModel = userRepository.findUserModelByUserId(incomingShopping.userId);
        shoppingRepository.save(incomingShopping);

        incomingShopping.shoppingItemList.forEach(item -> {
            if (item.shoppingItemId == -1) {
                var insertedItem = insertProductIfNeeded(incomingShopping, item , responseJson);
                newInserted.add(insertedItem.shoppingItemId);
            } else
                tempHashMap.put(item.shoppingItemId, item);
        });
        shoppingItemRepository.findShoppingItemByShoppingShoppingId(incomingShopping.shoppingId).forEach(item -> {
            if(newInserted.contains(item.shoppingItemId))
                return;
            if (!tempHashMap.containsKey(item.shoppingItemId))
                deleteList.add(item.shoppingItemId);
            else
                insertProductIfNeeded(incomingShopping, tempHashMap.get(item.shoppingItemId),responseJson);
        });
        shoppingItemRepository.deleteAllByIdInBatch(deleteList);
        responseJson.put(Constants.REMOTE_SHOPPING_ID,incomingShopping.getShoppingId());
        return responseJson;
    }
}
