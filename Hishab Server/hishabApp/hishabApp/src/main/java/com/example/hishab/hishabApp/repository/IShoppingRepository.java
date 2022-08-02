package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.Shopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingRepository extends JpaRepository<Shopping,Integer> {
}
