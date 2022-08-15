package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hishab.R
import com.example.hishab.usermanager.FirebaseOTPHelper
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import java.util.*


class LoginFragment : Fragment() {

    var isProcessing = false;
    lateinit var firebaseOTPHelper : FirebaseOTPHelper;


    var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
/*            p0.signIn
            firebaseOTPHelper.validCode = p0.smsCode
            isProcessing = false*/

        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(requireContext(),"Verification failed",Toast.LENGTH_LONG).show();
            isProcessing = false;
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            Toast.makeText(requireContext(),"Code Sent",Toast.LENGTH_LONG).show();
            firebaseOTPHelper.verificationId = p0
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container,OTPFragment()).commit()
            isProcessing = false;
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_login, container, false)
        firebaseOTPHelper = FirebaseOTPHelper.getInstance(requireActivity());
        Observable.create<EditText> { observableEmitter ->
            view.findViewById<Button>(R.id.btn_submit).setOnClickListener {
                observableEmitter.onNext(view.findViewById(R.id.et_phone_no))

            }
        }.filter(Predicate { !isProcessing && it.text.toString().isNotEmpty() }).subscribe {
            isProcessing = true
            firebaseOTPHelper.verify(it.text.toString(), callback)
        }

        return view;
    }


}


