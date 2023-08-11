package com.app.marier.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.marier.R
import com.app.marier.repository.UserModuleResponseRepository
import com.app.marier.utils.factories.USerViewModelFactory
import com.app.marier.viewmodel.UserModuleviewModel
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class SignupActivity : AppCompatActivity() {
    lateinit var viewModel: UserModuleviewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)



    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



}