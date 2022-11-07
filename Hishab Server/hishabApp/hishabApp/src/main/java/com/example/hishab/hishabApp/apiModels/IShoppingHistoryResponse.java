package com.example.hishab.hishabApp.apiModels;

import java.util.Date;

public interface IShoppingHistoryResponse {
	public int getCost();
	public String getDescription();
	public int getProductId();
	public int getShoppingItemId();
	public int getShoppingId();
	public Date getDate();
}
