package com.example.hishab.hishabApp.model;


import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String mobileNo;
    private String email;
    private String name;
    private String photoUrl;
    private String password;
}
