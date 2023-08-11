package com.app.marier.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.activities.SignupActivity
import com.app.marier.databinding.FragmentSignUpEmailBinding
import com.app.marier.datamodel.sociallogin.socialrequest.SocialRequest
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.JsonObject
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task



class SignUpEmailFragment : Fragment(), View.OnClickListener {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private var binding: FragmentSignUpEmailBinding? = null
    lateinit var viewModel: UserModuleviewModel
    private val RC_SIGN_IN = 9001
    private lateinit var callbackManager: CallbackManager




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpEmailBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        listeners()

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SignupActivity).viewModel
        listeners()
       gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            // Add additional scopes if needed
            .build()
         googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        callbackManager = CallbackManager.Factory.create()




    }

    private fun listeners() {
        binding?.tvcontinue?.setOnClickListener(this)
        binding?.tvtroublelogin?.setOnClickListener(this)
        binding?.termsandprivacypolicy?.setOnClickListener(this)
        binding?.imggoogle?.setOnClickListener(this)
        binding?.imgfacebook?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvcontinue -> {
                val jsonObject = JsonObject()
                if (!(binding?.etnumberemail!!.text.isEmpty())) {
                    if (binding?.etnumberemail?.text.toString().contains("@gmail.com")){
                        jsonObject.addProperty("email", binding?.etnumberemail?.text.toString())
                    }else{
                        jsonObject.addProperty("phoneNumber", binding?.etnumberemail?.text.toString())
                    }
                    viewModel.loginMethod(jsonObject)
                    bindobservers()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please Enter email or phonenumber",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            R.id.tvtroublelogin -> findNavController().navigate(R.id.action_signUpEmailFragment_to_loginPhoneNumberFragment)
            R.id.termsandprivacypolicy -> findNavController().navigate(R.id.action_signUpEmailFragment_to_termsconFragment)
            R.id.imggoogle->signIn()
            R.id.imgfacebook->{

                fbsignIn()
            }

        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun fbsignIn(){
        binding?.loginbutton?.performClick()
        binding?.loginbutton?.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken
                val userId = accessToken.userId
                Toast.makeText(requireContext(), "User ID: $userId", Toast.LENGTH_SHORT).show()
                val socialRequest = SocialRequest(userId!!,"facebook")
                viewModel.socialLoginAPi(socialRequest)
                bindobserverssociallogin()

            }

            override fun onCancel() {
                Toast.makeText(requireContext(), "cccccc", Toast.LENGTH_SHORT).show()

            }

            override fun onError(error: FacebookException) {
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()

            }
        })
    }


    private fun bindobservers() {
        viewModel.loginlivedata.value = null
        viewModel.loginlivedata.removeObservers(viewLifecycleOwner)
        viewModel.loginlivedata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    val bundle = bundleOf("myString" to binding?.etnumberemail?.text.toString())

                    findNavController().navigate(R.id.action_signUpEmailFragment_to_otpFragment,bundle)
                    CSPreferences.putString(requireActivity(),Utils.TOKEN,response?.data?.data?.token.toString())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            // Sign-in successful, handle authenticated user
            // Use account.getIdToken() to get the ID token for server-side validation
           val id =  account?.id
            Toast.makeText(requireContext(), id.toString(), Toast.LENGTH_SHORT).show()
            Log.d("googleid",id.toString())
            val socialreq = SocialRequest(id!!,"google")
            viewModel.socialLoginAPi(socialreq)
            bindobserverssociallogin()
            // ...
        } catch (e: ApiException) {
            // Sign-in failed, handle error
            // ...
            Toast.makeText(requireContext(), "ffff", Toast.LENGTH_SHORT).show()
            Log.d("chekingerror",e.toString())
        }
    }

    private fun bindobserverssociallogin () {
        viewModel.socialloginliveData.value = null
        viewModel.socialloginliveData.removeObservers(viewLifecycleOwner)
        viewModel.socialloginliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()

                    CSPreferences.putString(requireActivity(),Utils.TOKEN,response?.data?.token.toString())
                    CSPreferences.putString(requireActivity(),Utils.USERID,response?.data?.data?.id.toString())
                    findNavController().navigate(R.id.action_signUpEmailFragment_to_personalinfoFragment)
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