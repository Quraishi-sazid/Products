package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.model.Budget;
import com.example.hishab.hishabApp.model.Category;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.repository.IBudgetRepository;
import com.example.hishab.hishabApp.repository.IUserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/addOrUpdateBudget")
    Budget addBudget(@RequestParam("budgetId") int budgetId,
                     @RequestParam("budget") int budget,
                     @RequestParam("month") int month,
                     @RequestParam("year") int year,
                     @RequestParam("user_id") int userId,
                     @RequestParam("category_id") String categoryName) {
        UserModel userModel = userRepository.findUserModelByUserId(userId);
        Category category = categoryController.addCategory(categoryName);
        Budget budgetModel = new Budget(budget,month,year,userModel,category);
        if(budgetId!=-1)
            budgetModel.setBudgetId(budgetId);
        return budgetRepository.save(budgetModel);
    }
}
