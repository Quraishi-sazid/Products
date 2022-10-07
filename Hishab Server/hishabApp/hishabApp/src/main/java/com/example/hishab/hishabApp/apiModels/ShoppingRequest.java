package com.example.hishab.hishabApp.apiModels;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingRequest {
	int shoppingId;
	int userId;
	Date date;
	int localId;
	Long time;
	List<ShoppingItemRequest>shoppingItemRequests;
}
