package com.example.hishab.hishabApp.controller;


import com.example.hishab.hishabApp.model.UserDeviceModel;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.repository.IUserDeviceRepository;
import com.example.hishab.hishabApp.repository.IUserRepository;
import com.example.hishab.hishabApp.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import net.minidev.json.JSONObject;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:8080")
@RestController

@RequestMapping("/home/api")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;
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
            response.put("jwt",-1);
        } else {
            UserModel responseUserModel;
            UserDeviceModel responseUserDeviceModel = new UserDeviceModel();
            String phoneNo = (String) receivedObject.get("mobile");
            String deviceModel = (String) receivedObject.get("deviceModel");
            String firebaseId = (String) receivedObject.get("firebase_id");
            String jwtToken = JwtUtils.generateJwtToken(phoneNo);

            responseUserDeviceModel.setDeviceModel(deviceModel);
            responseUserDeviceModel.setFirebaseId(firebaseId);
            UserModel existingUser = userRepository.findFirstByMobileNo(phoneNo).orElse(null);
            if (existingUser == null) {
                UserModel newUser = new UserModel();
                newUser.setMobileNo(phoneNo);
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
            response.put("jwt:",jwtToken);
        }
        return response;
    }

    @GetMapping("/findAll/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<UserDeviceModel>findAll(@PathVariable int userId){
        return userDeviceRepository.findAllByUserUserId(userId);
    }

    @GetMapping("/findAll")
    public /*List<UserDeviceModel>*/ String findAllDeviceInforamtion(/*@RequestParam String userId*/){
       // return userDeviceRepository.findAllByUserName(userId);
        return "dfdfsdf";
    }

    @GetMapping("/test")
    public /*List<UserDeviceModel>*/ String test(/*@RequestParam String userId*/){
        // return userDeviceRepository.findAllByUserName(userId);
        return "testing done";
    }

    @PostMapping("/registration")
    public UserModel Registration(@RequestBody UserModel userModel) {
        if(userModel != null)
            return userRepository.save(userModel);
        return null;
    }
}
