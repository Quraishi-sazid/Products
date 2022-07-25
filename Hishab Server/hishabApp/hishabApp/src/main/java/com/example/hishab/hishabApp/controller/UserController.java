package com.example.hishab.hishabApp.controller;


import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.minidev.json.JSONObject;
@RestController
@RequestMapping("/home/api")
public class UserController {

    @Autowired
    IUserRepository userRepository;
    @PostMapping("/login")
    public UserModel login(@RequestBody JSONObject receivedObject){
        UserModel response = new UserModel();
        if(receivedObject.get("mobile") == null || ((String)receivedObject.get("mobile")).equals("")){
            response.setUserId(-1);
        }
        else{
            String phoneNo = (String)receivedObject.get("mobile");
            UserModel existingUser = userRepository.findFirstByMobileNo(phoneNo);
            if(existingUser == null){
                UserModel newUser = new UserModel();
                newUser.setMobileNo(phoneNo);
                newUser = userRepository.save(newUser);
                response = newUser;
            }
            else
                response = existingUser;
        }
        return  response;
    }

    @PostMapping("/Registration")
    public UserModel Registration(@RequestBody UserModel userModel){
        UserModel existingUser = userRepository.findFirstByMobileNoAndUserId(userModel.getMobileNo(),userModel.getUserId());
        if(existingUser == null){
            return null;
        }else {
            if(existingUser.getName() == null){
               return userRepository.save(userModel);
            }
            return null;

        }
    }
}
