package com.example.hishab.hishabApp.apiModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetItemResponse {
	public int localId;
	public int budgetId;
	public CategoryResponse categoryResponse;
}
