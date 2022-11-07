package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.apiModels.ICategoryProductResponse;
import com.example.hishab.hishabApp.apiModels.IBudgetHistoryResponse;
import com.example.hishab.hishabApp.model.Budget;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IBudgetRepository extends JpaRepository<Budget, Integer> {
	@Query(value = "SELECT category_id as categoryId,u.month as month,u.year as year,budget as budget,budget_id as budgetId FROM tbl_user_month_mapping u INNER JOIN tbl_budget b\r\n"
			+ "ON u.user_month_mapping_id= b.user_month_mapping_id\r\n"
			+ "WHERE  user_id = ?1", nativeQuery = true)
    List<IBudgetHistoryResponse> findCategoryBudget(Integer userId);
}
