package com.example.hishab.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.hishab.MainActivity
import com.example.hishab.R
import com.example.hishab.repository.IHistoryRepository
import com.example.hishab.retrofit.ApiCallStatus
import com.example.hishab.retrofit.ApiCaller
import com.example.hishab.retrofit.request.LoginRequest
import com.example.hishab.retrofit.response.LoginResponse
import com.example.hishab.usermanager.FirebaseOTPHelper
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import com.example.hishab.retrofit.RetrofitHelper
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.runBlocking


class OTPFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_o_t_p, container, false)
        view.findViewById<EditText>(R.id.et_otp).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 6) {
                    var otpHelper = FirebaseOTPHelper.getInstance(requireActivity())
                    val credential =
                        PhoneAuthProvider.getCredential(otpHelper.verificationId!!, text.toString())
                    otpHelper.validCode = credential.smsCode
                    if (!text.toString().equals(otpHelper.validCode)) {
                        Toast.makeText(requireContext(), "code not verified", Toast.LENGTH_LONG)
                            .show();
                    } else {
                        Toast.makeText(requireContext(), "code Verified", Toast.LENGTH_LONG).show();
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (task.isSuccessful && task.result != null) {
                                ApiCaller<LoginRequest,LoginResponse>(
                                    RetrofitHelper.hishabApi.login(
                                        LoginRequest(
                                            otpHelper.phoneNo,
                                            Build.MODEL,
                                            task.result
                                        )
                                    )
                                ).start().observe(viewLifecycleOwner) { response ->
                                    if (response.callStatus == ApiCallStatus.SUCCESS) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Logged in successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        if (response.response != null && response.response.body() != null) {
                                            PreferenceHelper.save(
                                                Constant.User_Id,
                                                response.response!!.body()!!.userId
                                            );
                                            PreferenceHelper.save(
                                                Constant.MobileNO
                                                ,otpHelper.phoneNo
                                            );
                                            PreferenceHelper.save(
                                                Constant.jwt,
                                                response.response!!.body()!!.jsonWebTokenJWT
                                            );
                                            if(response!!.response!!.body()!!.isNewUser){
                                                requireActivity().supportFragmentManager.beginTransaction()
                                                    .replace(R.id.container, RegisterFragment())
                                                    .commit()
                                            }
                                            else{
                                                runBlocking {
                                                    var historyRepository=IHistoryRepository(requireContext().applicationContext)
                                                    historyRepository.updateLocalDBFromRemote()
                                                }
                                                requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                                                requireActivity().finish()
                                            }

                                        }
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Logged in successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        return view
    }
}