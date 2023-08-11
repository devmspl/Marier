package com.app.marier.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.marier.databinding.ActivitySettingBinding
import com.app.marier.repository.UserModuleResponseRepository
import com.app.marier.utils.factories.USerViewModelFactory
import com.app.marier.viewmodel.UserModuleviewModel

class SettingActivity : AppCompatActivity() {
    lateinit var viewModel: UserModuleviewModel

    private var binding:ActivitySettingBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)

    }


}