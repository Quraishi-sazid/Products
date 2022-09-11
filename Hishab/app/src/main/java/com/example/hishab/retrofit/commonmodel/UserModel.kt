package com.example.hishab.retrofit.commonmodel;

import com.example.hishab.retrofit.JsonConverter
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper

data class UserModel(var userId: Int, var mobileNo: String, var name: String, var email: String) :JsonConverter() {
    var password: String? = null
    var photoUrl: String? = null

    fun saveInfoToPreference(){
        PreferenceHelper.save(Constant.User_Id,userId);
        PreferenceHelper.save(Constant.MobileNO,mobileNo);
        PreferenceHelper.save(Constant.UserName,name);
        PreferenceHelper.save(Constant.email,email)
        PreferenceHelper.save(Constant.photoUrl,photoUrl)
    }
}
