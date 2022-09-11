package com.example.hishab.hishabApp.apiModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	public Integer productId;
	public CategoryRequest categoryRequest;
	public String productName;
	public Integer userId;
	public int localId;
	
	public int getCategoryId() {
		return categoryRequest.getCategoryId();
	}
	public int getCategoryLocalId() {
		return categoryRequest.getLocalId();
	}
}
