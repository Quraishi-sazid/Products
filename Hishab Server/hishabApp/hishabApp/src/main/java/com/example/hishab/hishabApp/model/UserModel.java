package com.example.hishab.hishabApp.model;


import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String mobileNo;
    private String email;
    private String name;
}
