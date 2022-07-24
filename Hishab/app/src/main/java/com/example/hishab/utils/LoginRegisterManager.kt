package com.example.hishab.utils

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginRegisterManager private constructor(val activity:Activity) {
    companion object {
        val RC_SIGN_IN = 1
        private  var instance:LoginRegisterManager? = null
        fun getInstance(activity: Activity):LoginRegisterManager{
            if(instance == null)
                instance= LoginRegisterManager(activity)
            return instance!!
        }
    }
    private var mGoogleSignInClient: GoogleSignInClient
    private var gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    init {
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    fun loginWithGmail(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}