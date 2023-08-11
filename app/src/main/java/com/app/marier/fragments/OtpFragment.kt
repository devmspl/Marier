
package com.app.marier.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.activities.SignupActivity
import com.app.marier.databinding.FragmentOtpBinding
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel


class OtpFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentOtpBinding? = null
    lateinit var viewModel: UserModuleviewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        listeners()
        val myString = arguments?.getString("myString")


        binding?.tvedittext?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.otpview?.setOTP(s.toString())
            }
        })
        binding?.tvnumber?.text = "Please enter the 4-digit code sent to you at " +  myString




        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SignupActivity).viewModel
        listeners()
    }


    private fun listeners(){
        binding?.tvcontinue?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
        binding?.tvresend?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvcontinue->{
                if (!(binding?.otpview!!.otp.isEmpty())){
                    var token  = CSPreferences.readString(requireActivity(),Utils.TOKEN)
                    viewModel.otpMethod(token!!,binding?.otpview?.otp.toString())
                    bindobservers()
                }else{
                    Toast.makeText(requireContext(), "Please enter otp", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.imgback->activity?.finishAffinity()
            R.id.tvresend->{
                binding?.tvresend?.setBackgroundDrawable(resources.getDrawable(R.drawable.roundcornerappcolorsolid))
                binding?.tvresend?.setTextColor(Color.WHITE)
            }
        }
    }

    private fun bindobservers() {
        viewModel.otplivedata.value = null
        viewModel.otplivedata.removeObservers(viewLifecycleOwner)
        viewModel.otplivedata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    CSPreferences.putString(requireActivity(),Utils.TOKEN,response?.data?.token)
                    CSPreferences.putString(requireActivity(),Utils.USERID,response?.data?.data?.id)
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    if (response?.data?.data?.name!!.isNotEmpty()){
                        CSPreferences.putString(requireActivity(),Utils.USERLOGIN,"true")
                        startActivity(Intent(requireContext(),HomeActivity::class.java))
                        requireActivity().finishAffinity()
                    }else{

                        findNavController().navigate(R.id.action_otpFragment_to_termsandcondtionFragment)
                    }

                }
                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }

}
