package com.example.hishab.hishabApp.repository;


import com.example.hishab.hishabApp.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel,Integer> {
    Optional<UserModel> findFirstByMobileNo(String mobileNo);
    UserModel findFirstByMobileNoAndUserId(String mobileNo,int userId);
    UserModel findUserModelByUserId(int userId);
    Optional<UserModel> findFirstByName(String userName);
}
