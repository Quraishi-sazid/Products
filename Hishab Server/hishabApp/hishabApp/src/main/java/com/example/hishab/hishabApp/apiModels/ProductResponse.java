package com.example.hishab.hishabApp.apiModels;

import com.example.hishab.hishabApp.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProductResponse {
	String productName;
	int productId;
	int localId;
	CategoryResponse categoryResponse;
}
