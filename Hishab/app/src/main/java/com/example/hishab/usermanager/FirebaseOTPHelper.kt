package com.example.hishab.usermanager;

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseOTPHelper private constructor(val activity: Activity) {
    lateinit var phoneAuthOptions: PhoneAuthOptions;
    var validCode: String? = "";
    var phoneNo:String = "";
    var verificationId:String? ="";

    companion object {
        private var instance: FirebaseOTPHelper? = null;

        @Synchronized
        public fun getInstance(activity: Activity): FirebaseOTPHelper {
            if (instance == null)
                instance = FirebaseOTPHelper(activity)
            return instance!!;
        }
    }

    fun verify(
        phoneNo: String,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    ) {
        this.phoneNo = phoneNo
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNo) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(
                callback ?: this.verificationCallbacks
            ) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private var verificationCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

            }
        }

}
