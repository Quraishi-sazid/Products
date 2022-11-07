package com.example.hishab.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hishab.MainActivity
import com.example.hishab.R
import com.example.hishab.databinding.FragmentRegisterBinding
import com.example.hishab.retrofit.ApiCallStatus
import com.example.hishab.retrofit.ApiCaller
import com.example.hishab.retrofit.commonmodel.UserModel
import com.example.hishab.usermanager.GmailLoginHelper
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import com.example.hishab.retrofit.RetrofitHelper
import java.util.regex.Pattern

class RegisterFragment : Fragment() {
    var inflate: View? = null
    lateinit var binding: FragmentRegisterBinding
    val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        initialize();
        return binding.root
    }

    private fun initialize() {
        binding.submitButton.setOnClickListener {
            if (!verifyEmailAndUserName())
                Toast.makeText(
                    requireContext(),
                    "please enter name and username Correctly",
                    Toast.LENGTH_LONG
                ).show();
            else {
                var userModel = UserModel(
                    PreferenceHelper.get(Constant.User_Id, -1),
                    PreferenceHelper.get(Constant.MobileNO, "")!!,
                    binding.etUserName.text.toString(),
                    binding.etUserEmail.text.toString()
                )
            }
        }
        binding.signInButton.setOnClickListener {
            var gmailLoginHelper = GmailLoginHelper.getInstance(requireActivity())
            gmailLoginHelper.onAccountResultSuccess.observe(viewLifecycleOwner, { account ->
                if (gmailLoginHelper.isValid(account)) {
                    var userModel = UserModel(
                        PreferenceHelper.get(Constant.User_Id, -1),
                        PreferenceHelper.get(Constant.MobileNO, "")!!,
                        account.displayName!!,
                        account.email!!
                    )
                    userModel.photoUrl = account.photoUrl?.path
                    callApi(userModel)
                }
            })
            gmailLoginHelper.login()
        }
    }

    fun callApi(userModel: UserModel){
        ApiCaller<UserModel,UserModel>(RetrofitHelper.hishabApi.registration(userModel)).start().observe(viewLifecycleOwner) { response ->
            if (response.callStatus == ApiCallStatus.SUCCESS) {
                response.response?.body()?.saveInfoToPreference()
                requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }else{
                Toast.makeText(requireContext(),"server error",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun verifyEmailAndUserName(): Boolean {
        return binding.etUserName.text != null && !binding.etUserName.text.toString().equals("") && VALID_EMAIL_ADDRESS_REGEX.matcher(
            binding.etUserEmail.text.toString()
        )
            .find()
    }


}