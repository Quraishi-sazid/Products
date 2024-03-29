package com.example.hishab.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id") var userId: Int,
    @SerializedName("user_device_id") var userDeviceId: Int,
    @SerializedName("jwt:") var jsonWebTokenJWT: String,
    @SerializedName("is_new_user") var isNewUser: Boolean
) {
}