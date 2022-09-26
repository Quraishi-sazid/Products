package com.example.hishab.hishabApp.model;


import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.*;
@Data
@Entity
@Table(name = "tbl_product")
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String productName;
/*    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;*/

    public Product() {
    }

    public Product(String productName) {
        this.productName = productName;
    }
    public Product(int id, String productName) {
		this.productId = id;
		this.productName = productName;
	}
	@Transient
    private int categoryId;
}
