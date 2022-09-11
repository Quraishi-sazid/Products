package com.example.hishab.hishabApp.apiModels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingItemResponse {
	private int localId;
	private int shoppingItemId;
	private ProductResponse productResponse;
}
