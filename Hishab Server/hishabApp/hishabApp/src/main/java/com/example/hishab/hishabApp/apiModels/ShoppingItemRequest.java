package com.example.hishab.hishabApp.apiModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingItemRequest {
	int shoppingItemId;
	int cost;
	String descString;
	int localId;
	ProductRequest productRequest;
	
	public int getCategoryId(){
		return productRequest.getCategoryId();
	}
	public int getCategoryLocalId(){
		return productRequest.getCategoryLocalId();
	}
	
	public int getProductId(){
		return productRequest.getProductId();
	}
	public int getProductLocalId(){
		return productRequest.getLocalId();
	}
}
