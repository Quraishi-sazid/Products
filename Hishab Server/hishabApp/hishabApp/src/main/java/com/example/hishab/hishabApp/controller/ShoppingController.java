package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.apiModels.CategoryResponse;
import com.example.hishab.hishabApp.apiModels.ProductRequest;
import com.example.hishab.hishabApp.apiModels.ProductResponse;
import com.example.hishab.hishabApp.apiModels.ShoppingItemRequest;
import com.example.hishab.hishabApp.apiModels.ShoppingItemResponse;
import com.example.hishab.hishabApp.apiModels.ShoppingRequest;
import com.example.hishab.hishabApp.apiModels.ShoppingResponse;
import com.example.hishab.hishabApp.model.Product;
import com.example.hishab.hishabApp.model.Shopping;
import com.example.hishab.hishabApp.model.ShoppingItem;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.repository.IShoppingItemRepository;
import com.example.hishab.hishabApp.repository.IShoppingRepository;
import com.example.hishab.hishabApp.repository.IUserRepository;

import org.hibernate.boot.model.relational.InitCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

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

	@Transactional
	@PostMapping("/addShopping")
	public ShoppingResponse addShopping(@RequestBody ShoppingRequest incomingShopping) {
		ShoppingResponse shoppingResponse = new ShoppingResponse(incomingShopping.getShoppingId(),
				incomingShopping.getLocalId(), new ArrayList<ShoppingItemResponse>());
		UserModel userModel = userRepository.findUserModelByUserId(incomingShopping.getUserId());
		Shopping insertedShopping = shoppingRepository.save(new Shopping(incomingShopping.getTime(), userModel));
		incomingShopping.getShoppingItemRequests().forEach(item -> {
			shoppingResponse.getItemResponses()
					.add(shoppingItemToShoppingItemResponse(item, insertProductIfNeeded(insertedShopping, item)));
		});
		shoppingResponse.setShoppingId(insertedShopping.shoppingId);
		return shoppingResponse;
	}

	private ShoppingItemResponse shoppingItemToShoppingItemResponse(ShoppingItemRequest shoppingItemRequest,
			ShoppingItem shoppingItem) {
		CategoryResponse categoryResponse = new CategoryResponse(shoppingItemRequest.getCategoryLocalId(),
				shoppingItem.product.getCategoryId());
		ProductResponse productResponse = new ProductResponse(shoppingItem.product.getProductName(),
				shoppingItem.product.getProductId(), shoppingItemRequest.getProductLocalId(), categoryResponse);
		return new ShoppingItemResponse(shoppingItemRequest.getLocalId(), shoppingItem.shoppingItemId, productResponse);
	}

	private ShoppingItem insertProductIfNeeded(Shopping insertedShopping, ShoppingItemRequest item) {
		ShoppingItem shoppingItem = new ShoppingItem(item.getShoppingItemId(), item.getCost(), item.getDescString());
		if (item.getProductRequest().getProductId() == -1) {
			ProductResponse productResponse = productController.addOrUpdateProduct(new ProductRequest(-1,
					item.getProductRequest().categoryRequest, item.getProductRequest().getProductName(),
					insertedShopping.userModel.getUserId(), item.getProductRequest().getLocalId()));
			shoppingItem.setProduct(new Product(productResponse.getProductId(), productResponse.getProductName(),
					productResponse.getCategoryResponse().getCategoryId()));
		}
		else {
			shoppingItem.setProduct(new Product(item.getProductRequest().productId,item.getProductRequest().productName,item.getProductRequest().getCategoryId()));
		}
		if (shoppingItem.getProduct().getProductId() != -1) {
			shoppingItem.setShopping(insertedShopping);
			ShoppingItem insertedItem = shoppingItemRepository.save(shoppingItem);
			insertedItem.getProduct().setCategoryId(shoppingItem.getProduct().getCategoryId());
			return insertedItem;
		}
		return null;
	}

	@Transactional
	@PostMapping("/updateShopping")
	ShoppingResponse updateShoppingItem(@RequestBody ShoppingRequest incomingShopping) {
		HashMap<Integer, ShoppingItemRequest> tempHashMap = new HashMap<Integer, ShoppingItemRequest>();
		HashSet<Integer> newInserted = new HashSet<Integer>();
		ShoppingResponse shoppingResponse = new ShoppingResponse(incomingShopping.getShoppingId(),
				incomingShopping.getLocalId(), new ArrayList<ShoppingItemResponse>());
		List<Integer> deleteList = new ArrayList<>();
		UserModel userModel = userRepository.findUserModelByUserId(incomingShopping.getUserId());
		Shopping shopping = shoppingRepository
				.save(new Shopping(incomingShopping.getTime(), userModel, incomingShopping.getShoppingId()));
		incomingShopping.getShoppingItemRequests().forEach(item -> {
			if (item.getShoppingItemId() == -1) {
				var insertedItem = insertProductIfNeeded(shopping, item);
				shoppingResponse.getItemResponses()
						.add(shoppingItemToShoppingItemResponse(item, insertProductIfNeeded(shopping, item)));
				newInserted.add(insertedItem.shoppingItemId);
			} else
				tempHashMap.put(item.getShoppingItemId(), item);
		});

		
		shoppingItemRepository.findShoppingItemByShoppingShoppingId(incomingShopping.getShoppingId()).forEach(item -> {
			if (newInserted.contains(item.shoppingItemId))
				return;
			if (!tempHashMap.containsKey(item.shoppingItemId))
				deleteList.add(item.shoppingItemId);
			else {
				ShoppingItem updatedItem = insertProductIfNeeded(shopping, tempHashMap.get(item.shoppingItemId));
				shoppingResponse.getItemResponses().add(
						shoppingItemToShoppingItemResponse(tempHashMap.get(updatedItem.shoppingItemId), updatedItem));
			}

		});
		shoppingItemRepository.deleteAllByIdInBatch(deleteList);
		return shoppingResponse;
	}

	@Transactional
	@PostMapping("/updateShoppingList")
	public List<ShoppingResponse> updateShoppingList(@RequestBody List<ShoppingRequest> incomingShoppingList) {
		List<ShoppingResponse> shoppingResponses = new ArrayList<ShoppingResponse>();
		incomingShoppingList.forEach(item -> {
			shoppingResponses.add(updateShoppingItem(item));
		});
		return shoppingResponses;
	}

	@Transactional
	@PostMapping("/addShoppingList")
	public List<ShoppingResponse> addShoppingList(@RequestBody List<ShoppingRequest> incomingShoppingList) {
		List<ShoppingResponse> shoppingResponses = new ArrayList<ShoppingResponse>();
		incomingShoppingList.forEach(item -> {
			shoppingResponses.add(addShopping(item));
		});
		return shoppingResponses;
	}

}
