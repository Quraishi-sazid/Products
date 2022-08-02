package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBudgetRepository extends JpaRepository<Budget, Integer> {

}
