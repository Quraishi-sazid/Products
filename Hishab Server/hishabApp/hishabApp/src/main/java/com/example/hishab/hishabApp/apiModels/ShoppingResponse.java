package com.example.hishab.hishabApp.apiModels;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingResponse {
	private int shoppingId;
	private List<ShoppingItemResponse>itemResponses;
	
}
