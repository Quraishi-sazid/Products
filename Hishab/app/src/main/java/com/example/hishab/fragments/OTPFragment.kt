package com.example.hishab.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.hishab.R
import com.example.hishab.usermanager.FirebaseOTPHelper
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import com.google.firebase.auth.PhoneAuthProvider


class OTPFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_o_t_p, container, false)
        view.findViewById<EditText>(R.id.et_otp).addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(text?.length == 6){
                    val credential = PhoneAuthProvider.getCredential(FirebaseOTPHelper.getInstance(requireActivity()).verificationId!!, text.toString())
                    FirebaseOTPHelper.getInstance(requireActivity()).validCode = credential.smsCode
                    if(!text.toString().equals(FirebaseOTPHelper.getInstance(requireActivity()).validCode)){
                        Toast.makeText(requireContext(),"code not verified",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(requireContext(),"code Verified",Toast.LENGTH_LONG).show();
                        PreferenceHelper.save(Constant.User_Id,1);
                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container,RegisterFragment()).commit()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        return view
    }
}