package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.apiModels.IShoppingHistoryResponse;
import com.example.hishab.hishabApp.model.Shopping;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingRepository extends JpaRepository<Shopping,Integer> {
	
	@Query(value = "SELECT cost as cost,description as description,product_id as productId,shopping_item_id as shoppingItemId,s.shopping_id as shoppingId,s.date as date FROM tbl_shopping s\r\n"
			+ "INNER JOIN tbl_shopping_item si on s.shopping_id = si.shopping_id WHERE s.user_id = 10",nativeQuery = true)
	List<IShoppingHistoryResponse>findShoppingHistory(int userId);
	
}
