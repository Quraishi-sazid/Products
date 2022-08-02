package com.example.hishab.hishabApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
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

    @JsonInclude()
    @Transient
    public int userId;

    /*    @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name ="s_id",nullable = false)
      //  @OnDelete(action = OnDeleteAction.CASCADE)
        @JsonIgnore*/
    @JsonInclude()
    @Transient
    public List<ShoppingItem> shoppingItemList;
}
