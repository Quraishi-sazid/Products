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
	public ShoppingResponse addShopping(@RequestBody ShoppingRequest incomingShopping) {
		ShoppingResponse shoppingResponse = new ShoppingResponse(incomingShopping.getShoppingId(),new ArrayList<ShoppingItemResponse>());
		UserModel userModel = userRepository.findUserModelByUserId(incomingShopping.getUserId());
		Shopping insertedShopping = shoppingRepository.save(new Shopping(incomingShopping.getDate(),userModel));
		incomingShopping.getShoppingItemRequests().forEach(item -> {
			shoppingResponse.getItemResponses().add( shoppingItemToShoppingItemResponse(item,insertProductIfNeeded(insertedShopping, item)));
		});
		/*
		 * List<ShoppingItem> shoppingItemList =
		 * incomingShopping.getShoppingItemRequests(); for (int i = 0; i <
		 * shoppingItemList.size(); i++) { shoppingItemList.set(i,
		 * insertProductIfNeeded(insertedShopping, shoppingItemList.get(i))); }
		 * insertedShopping.shoppingItemList = shoppingItemList;
		 * 
		 * new ArrayList<ShoppingItemResponse>()); shoppingItemList.forEach(shoppingItem
		 * -> { shoppingResponse.getItemResponses().add(new
		 * ShoppingItemResponse(shoppingItem.localId, shoppingItem.shoppingItemId, new
		 * ProductResponse(shoppingItem.product.productId,
		 * shoppingItem.product.categoryId))); });
		 */
		return shoppingResponse;
	}

	
	private ShoppingItemResponse shoppingItemToShoppingItemResponse(ShoppingItemRequest shoppingItemRequest,ShoppingItem shoppingItem) {
		CategoryResponse categoryResponse = new CategoryResponse(shoppingItemRequest.getCategoryLocalId(),shoppingItemRequest.getCategoryId());
		ProductResponse productResponse = new ProductResponse(shoppingItem.product.productName,shoppingItem.product.productId,shoppingItemRequest.getProductLocalId(),categoryResponse);
		return new ShoppingItemResponse(shoppingItemRequest.getLocalId(),shoppingItem.shoppingItemId,productResponse);
	}
	/*
	 * private ShoppingItem insertProductIfNeeded(Shopping insertedShopping,
	 * ShoppingItem item) { if (item.getProduct().getProductId() == -1) { if
	 * (item.getCategoryName() != null && item.getCategoryName() != "") {
	 * item.setProduct(productController.addOrUpdateProduct(new ProductRequest(-1,
	 * -1, item.getCategoryName(), item.getProduct().getProductName(),
	 * insertedShopping.userModel.getUserId())).getProduct()); } } if
	 * (item.getProduct().getProductId() != -1) {
	 * item.setShopping(insertedShopping); ShoppingItem insertedItem =
	 * shoppingItemRepository.save(item); return insertedItem; } return null; }
	 */
	
	private ShoppingItem insertProductIfNeeded(Shopping insertedShopping, ShoppingItemRequest item) {
		ShoppingItem shoppingItem = new ShoppingItem();
		if (item.getProductRequest().getProductId() == -1) {
			if (item.getProductRequest().categoryRequest != null && item.getProductRequest().categoryRequest.getNewCategoryName() != "") {
				ProductResponse productResponse = productController.addOrUpdateProduct(new ProductRequest(-1,item.getProductRequest().categoryRequest,
						item.getProductRequest().getProductName(),insertedShopping.userModel.getUserId(), item.getLocalId())); 
				shoppingItem.setProduct(new Product(productResponse.getProductId(),productResponse.getProductName(),productResponse.getCategoryResponse().getCategoryId()));
			}
		}
		if (shoppingItem.getProduct().getProductId() != -1) {
			shoppingItem.setShopping(insertedShopping);
			ShoppingItem insertedItem = shoppingItemRepository.save(shoppingItem);
			return insertedItem;
		}
		return null;
	}

	@PostMapping("/updateShopping")
	ShoppingResponse updateShoppingItem(@RequestBody ShoppingRequest incomingShopping) {
		HashMap<Integer, ShoppingItemRequest> tempHashMap = new HashMap<Integer, ShoppingItemRequest>();
		HashSet<Integer> newInserted = new HashSet<Integer>();
		ShoppingResponse shoppingResponse = new ShoppingResponse(incomingShopping.getShoppingId(),
				new ArrayList<ShoppingItemResponse>());
		List<Integer> deleteList = new ArrayList<>();
		UserModel userModel  = userRepository.findUserModelByUserId(incomingShopping.getUserId());
		Shopping shopping = shoppingRepository.save(new Shopping(incomingShopping.getDate(),userModel,incomingShopping.getUserId()));
		incomingShopping.getShoppingItemRequests().forEach(item -> {
			if (item.getShoppingItemId() == -1) {
				var insertedItem = insertProductIfNeeded(shopping, item);
				shoppingResponse.getItemResponses().add(shoppingItemToShoppingItemResponse(item,insertProductIfNeeded(shopping, item)));
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
				ShoppingItem updatedItem = insertProductIfNeeded(shopping,
						tempHashMap.get(item.shoppingItemId));
				shoppingResponse.getItemResponses().add(shoppingItemToShoppingItemResponse(tempHashMap.get(updatedItem.shoppingItemId),updatedItem));
			}

		});
		shoppingItemRepository.deleteAllByIdInBatch(deleteList);
		return shoppingResponse;
	}
	
	
	@PostMapping("/updateShoppingList")
	public List<ShoppingResponse> updateShoppingList(@RequestBody  List<ShoppingRequest> incomingShoppingList) {
		List<ShoppingResponse>shoppingResponses = new ArrayList<ShoppingResponse>();
		incomingShoppingList.forEach(item->{
			 shoppingResponses.add(updateShoppingItem(item)); 
		});
		return shoppingResponses;
	}
	@PostMapping("/addShoppingList")
	public List<ShoppingResponse> addShoppingList(@RequestBody  List<ShoppingRequest> incomingShoppingList) {
		List<ShoppingResponse>shoppingResponses = new ArrayList<ShoppingResponse>();
		incomingShoppingList.forEach(item->{
			 shoppingResponses.add(addShopping(item)); 
		});
		return shoppingResponses;
	}
	
	
}
