package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.apiModels.BudgetItemResponse;
import com.example.hishab.hishabApp.apiModels.BudgetRequest;
import com.example.hishab.hishabApp.apiModels.BudgetResponse;
import com.example.hishab.hishabApp.apiModels.CategoryResponse;
import com.example.hishab.hishabApp.model.Budget;
import com.example.hishab.hishabApp.model.Category;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.model.UserMonthMapping;
import com.example.hishab.hishabApp.repository.IBudgetRepository;
import com.example.hishab.hishabApp.repository.IUserRepository;
import com.example.hishab.hishabApp.repository.UserMonthRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@RequestMapping("/budget/api")
@RestController
public class BudgetController {
    @Autowired
    IBudgetRepository budgetRepository;
    @Autowired
    IUserRepository userRepository;

    @Autowired
    CategoryController categoryController;
    @Autowired
    UserMonthRepository userMonthRepository;

    @PostMapping("/addOrUpdateBudget")
    BudgetResponse addOrUpdateBudget(@RequestBody BudgetRequest budgetRequest) {
    	BudgetResponse budgetResponse = new BudgetResponse();
    	budgetResponse.budgetItemResponses = new ArrayList<>();
        UserModel userModel = userRepository.findUserModelByUserId(budgetRequest.userId);
        UserMonthMapping userMonthMapping = userMonthRepository.findFirstUserMonthMappingByYearAndMonthAndUserUserIdEquals(budgetRequest.year, budgetRequest.month, userModel.getUserId());
        if(userMonthMapping == null)
        	userMonthMapping = userMonthRepository.save(new UserMonthMapping(budgetRequest.year,budgetRequest.month,userModel));
        final UserMonthMapping umMapping = userMonthMapping;
        budgetRequest.budgetItemRequests.forEach(request->{
        	Category category = categoryController.addCategory(request.categoryRequest.newCategoryName);
        	CategoryResponse catResponse = new CategoryResponse(request.categoryRequest.getLocalId(), category.getCategoryId());
            Budget budgetModel = new Budget(request.budget,umMapping,category);
            if(request.budgetItemId!=-1)
                budgetModel.setBudgetId(request.budgetItemId);
            budgetModel = budgetRepository.save(budgetModel);
            budgetResponse.budgetItemResponses.add(new BudgetItemResponse(request.localId,budgetModel.getBudgetId(),catResponse));
        });
        return budgetResponse;
    }
    
    @PostMapping("/addOrUpdateBudgetList")
    List<BudgetResponse> addOrUpdateBudgetList(List<BudgetRequest>budgetRequestList){
    	List<BudgetResponse>responseList = new ArrayList<BudgetResponse>();
    	budgetRequestList.forEach(req->{
    		responseList.add(addOrUpdateBudget(req));
    	});
    	return responseList;
    }
}
