package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_shopping")
public class Shopping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int shoppingId;
    public Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public UserModel userModel;
    
    
    public Shopping(Long time,UserModel userModel,int shoppingId) {
		this.date = new Date(time);
		this.userModel = userModel;
		this.shoppingId = shoppingId;
	}
    public Shopping(Long time,UserModel userModel) {
		this.date = new Date(time);
		this.userModel = userModel;
	}

    @JsonInclude()
    @Transient
    public int userId;


    @JsonInclude()
    @Transient
    public List<ShoppingItem> shoppingItemList;
}
