package com.example.hishab.usermanager

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.example.hishab.interfaces.ILoginHelper
import com.example.hishab.utils.LoginRegisterManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GmailLoginHelper  private constructor(val activity: Activity) : ILoginHelper{
    var onAccountResultSuccess = MutableLiveData<GoogleSignInAccount>();

    companion object {
        var instance:GmailLoginHelper?=null
        public val RC_SIGN_IN = 1
        @Synchronized
        fun getInstance(activity: Activity):GmailLoginHelper {
            if(instance == null)
                instance = GmailLoginHelper(activity)
            return instance!!
        }
    }

    private var gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    private var mGoogleSignInClient: GoogleSignInClient

    init {
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    override fun login() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun isValid(account: GoogleSignInAccount?): Boolean {
        if(account!=null && account.displayName!=null && account.email!=null)
            return true
        return false
    }


    /*
     put those codes in activity
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

         if (requestCode == GmailLoginHelper.RC_SIGN_IN) {
             val task = GoogleSignIn.getSignedInAccountFromIntent(data)
             handleSignInResult(task)
         }
     }

     private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
         try {
             val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
             Log.v("signindata",account.email!!)
         } catch (e: ApiException) {
         }
     }*/
}