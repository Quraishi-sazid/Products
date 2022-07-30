package com.example.hishab.hishabApp.controller;


import com.example.hishab.hishabApp.model.UserDeviceModel;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.repository.IUserDeviceRepository;
import com.example.hishab.hishabApp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.minidev.json.JSONObject;

import java.util.List;

@RestController
@RequestMapping("/home/api")
public class UserController {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    IUserDeviceRepository userDeviceRepository;

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject receivedObject) {
        JSONObject response = new JSONObject();
        if (receivedObject.get("mobile") == null || ((String) receivedObject.get("mobile")).equals("")) {
            response.put("user_id", -1);
            response.put("user_device_id", -1);
        } else {
            UserModel responseUserModel;
            UserDeviceModel responseUserDeviceModel = new UserDeviceModel();
            String phoneNo = (String) receivedObject.get("mobile");
            String deviceModel = (String) receivedObject.get("deviceModel");
            String firebaseId = (String) receivedObject.get("firebase_id");
            responseUserDeviceModel.setDeviceModel(deviceModel);
            responseUserDeviceModel.setFirebaseId(firebaseId);
            UserModel existingUser = userRepository.findFirstByMobileNo(phoneNo);
            if (existingUser == null) {
                UserModel newUser = new UserModel();
                newUser.setMobileNo(phoneNo);
                //newUser.setUserId(1);
                responseUserModel = userRepository.save(newUser);
                responseUserDeviceModel.setUser(responseUserModel);
                responseUserDeviceModel = userDeviceRepository.save(responseUserDeviceModel);
            } else {
                responseUserModel = existingUser;
                responseUserDeviceModel.setUser(responseUserModel);
                try {
                    responseUserDeviceModel = userDeviceRepository.save(responseUserDeviceModel);
                } catch (Exception ex) {
                    responseUserDeviceModel = userDeviceRepository.findByFirebaseId(firebaseId);
                }
            }
            response.put("user_id", responseUserModel.getUserId());
            response.put("user_device_id", responseUserDeviceModel.getUserDeviceId());
        }
        return response;
    }

    @GetMapping("/findAll/{userId}")
    public List<UserDeviceModel>findAll(@PathVariable int userId){
        return userDeviceRepository.findAllByUserUserId(userId);
    }

    /*@GetMapping("/findAll")
    public List<UserDeviceModel> findAllDeviceInforamtion(@RequestParam String userId){
        return userDeviceRepository.findAllByUserName(userId);
    }*/

    @PostMapping("/Registration")
    public UserModel Registration(@RequestBody UserModel userModel) {
        UserModel existingUser = userRepository.findFirstByMobileNoAndUserId(userModel.getMobileNo(), userModel.getUserId());
        if (existingUser == null) {
            return null;
        } else {
            if (existingUser.getName() == null) {
                return userRepository.save(userModel);
            }
            return null;

        }
    }
}
