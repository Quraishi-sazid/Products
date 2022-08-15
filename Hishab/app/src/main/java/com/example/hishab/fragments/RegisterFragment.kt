package com.example.hishab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hishab.R
import com.example.hishab.databinding.FragmentRegisterBinding
import com.example.hishab.usermanager.GmailLoginHelper
import java.util.regex.Pattern

class RegisterFragment : Fragment() {

    var inflate:View?=null
    lateinit var binding:FragmentRegisterBinding
    val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false);
        initilize();
        return binding.root
    }

    private fun initilize() {
        binding.submitButton.setOnClickListener{
            if(!verifyEmailAndUserName())
                Toast.makeText(requireContext(),"please enter name and username Correctly",Toast.LENGTH_LONG).show();
            else{

            }
        }
        binding.signInButton.setOnClickListener{
            var gmailLoginHelper = GmailLoginHelper(requireActivity())
            gmailLoginHelper.login()
        }
    }

    private fun verifyEmailAndUserName():Boolean {
        return !binding.etUserName.text.equals("") && VALID_EMAIL_ADDRESS_REGEX.matcher(binding.etUserName.text.toString()).find()
    }


}