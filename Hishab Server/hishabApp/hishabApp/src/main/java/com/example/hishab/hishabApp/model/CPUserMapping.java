package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_cp_user_mapping")
public class CPUserMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cpUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_product_mapping_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CategoryAndProductMapping categoryProductMapping;

    public CPUserMapping(CategoryAndProductMapping categoryAndProductMapping, UserModel userModel) {
        this.categoryProductMapping = categoryAndProductMapping;
        this.user = userModel;
    }

    public CPUserMapping() {

    }
}
