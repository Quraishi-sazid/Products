package com.example.hishab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hishab.fragments.LoginFragment
import com.example.hishab.fragments.RegisterFragment
import com.example.hishab.models.loginRequest
import com.example.hishab.usermanager.GmailLoginHelper
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import com.example.hishab.utils.RetrofitHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        var aaa= RetrofitHelper.hishabApi.login(loginRequest("+8801781888888","sss","1234"))

        determineUserLoggedInState()

    }

    private fun determineUserLoggedInState() {
        if (PreferenceHelper.get(Constant.User_Id, -1) == -1) {
            supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment())
                .commit()
        } else if (PreferenceHelper.get(Constant.UserName, null) == null)
            supportFragmentManager.beginTransaction().replace(R.id.container, RegisterFragment())
                .commit()
        else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

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
    }

}