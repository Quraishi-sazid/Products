package com.example.hishab.hishabApp.apiModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
	public int categoryId;
	public String newCategoryName;
	public String oldCategoryName;
	public int localId;
	
	public CategoryRequest(int categoryId, String newCategoryName, int localId) {
		this.categoryId = categoryId;
		this.newCategoryName = newCategoryName;
		this.localId = localId;
	}
}
