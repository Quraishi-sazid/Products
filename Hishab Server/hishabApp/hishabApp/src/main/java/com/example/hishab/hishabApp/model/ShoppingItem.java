package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_shopping_item")
public class ShoppingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_item_id",nullable = false)
    public int shoppingItemId;
    public int cost;
    public String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopping_id", nullable = false)
   // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public Shopping shopping;
    
    public ShoppingItem() {}
    
    public ShoppingItem(int shoppingItemId,int cost,String descString) {
    	this.cost = cost;
    	this.description = descString;
    	this.shoppingItemId = shoppingItemId;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Product product;

    @JsonInclude()
    @Transient
    public String categoryName;

    @JsonInclude()
    @Transient
    public int localId;
}
