package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_budget")
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
   
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int budgetId;

    private int budget;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_month_mapping_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserMonthMapping userMonthMapping;
    

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;
    
    public Budget(int budget, UserMonthMapping userMonthMapping, Category category) {
		this.budget = budget;
		this.userMonthMapping = userMonthMapping;
		this.category = category;
	}
}
