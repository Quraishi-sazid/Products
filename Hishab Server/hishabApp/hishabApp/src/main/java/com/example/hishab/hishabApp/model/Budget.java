package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int budgetId;

    private int budget;
    private int month;
    private int year;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserModel user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    Budget(){}

    public Budget(int budget, int month, int year, UserModel user, Category category) {
        this.budget = budget;
        this.month = month;
        this.year = year;
        this.user = user;
        this.category = category;
    }
}
