package com.example.hishab.hishabApp.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int categoryId;
    @Column(unique = true)
    String categoryName;
    public Category(){}
    public Category(String categoryName){
        this.categoryName = categoryName;
    }
    public Category(int categoryId,String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}
