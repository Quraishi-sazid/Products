package com.example.hishab.hishabApp.apiModels;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetRequest {
	public int year;
	public int month;
	public int userId;
	public List<BudgetItemRequest>budgetItemRequests;
}
