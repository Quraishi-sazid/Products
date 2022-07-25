package com.example.hishab.hishabApp.repository;


import com.example.hishab.hishabApp.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserModel,Integer> {
    UserModel findFirstByMobileNo(String mobileNo);
    UserModel findFirstByMobileNoAndUserId(String mobileNo,int userId);
}
