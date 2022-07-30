package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.UserDeviceModel;
import com.example.hishab.hishabApp.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDeviceRepository extends JpaRepository<UserDeviceModel,Integer> {
        List<UserDeviceModel> findAllByUserUserId(int userId);
        UserDeviceModel findByFirebaseId(String firebaseId);
       List<UserDeviceModel> findAllByUserName(String name);
}
