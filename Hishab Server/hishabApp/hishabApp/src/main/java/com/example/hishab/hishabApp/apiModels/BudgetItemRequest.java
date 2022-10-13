package com.example.hishab.hishabApp.apiModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetItemRequest {
	public CategoryRequest categoryRequest;
	public int budgetItemId;
	public int budget;
	public int localId;
}
