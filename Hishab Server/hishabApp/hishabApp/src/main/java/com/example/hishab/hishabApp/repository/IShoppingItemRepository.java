package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.QueryEval;
import java.util.List;

@Repository
public interface IShoppingItemRepository extends JpaRepository<ShoppingItem,Integer> {

    List<ShoppingItem> findShoppingItemByShoppingShoppingId(int shoppingId);

    /*Query("DELETE FROM tbl_shopping_item WHERE shopping_item_id NOT IN :  ",nativeQuery=true)
        void deleteShoppingItemByShoppingShoppingId*/
}
