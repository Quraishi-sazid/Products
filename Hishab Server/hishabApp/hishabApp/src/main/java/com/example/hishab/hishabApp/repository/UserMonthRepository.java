package com.example.hishab.hishabApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hishab.hishabApp.model.Product;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.model.UserMonthMapping;

public interface UserMonthRepository extends JpaRepository<UserMonthMapping, Integer> {
	UserMonthMapping findFirstUserMonthMappingByYearAndMonthAndUserUserIdEquals(int year,int month,int userId);
}
