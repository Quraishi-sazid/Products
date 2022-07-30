package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_category_product_mapping")
public class CategoryAndProductMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryProductMappingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    public CategoryAndProductMapping(Category productCategory, Product insertedProduct) {
        this.product = insertedProduct;
        this.category =  productCategory;
    }

    public CategoryAndProductMapping() {

    }
}
