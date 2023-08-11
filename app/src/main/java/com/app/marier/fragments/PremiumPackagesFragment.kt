package com.app.marier.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.marier.R
import com.app.marier.activities.PaidcoinConnectionActivity
import com.app.marier.activities.PremiumSubcriptionActivity
import com.app.marier.databinding.FragmentPremiumPackagesBinding


class PremiumPackagesFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentPremiumPackagesBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPremiumPackagesBinding.inflate(layoutInflater, container, false)
        listeners()
        return binding!!.root
    }

    private fun listeners() {
        binding?.tvgetyourpremium?.setOnClickListener(this)
        binding?.tvgetpaidcoins?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvgetyourpremium -> {
                var intent = Intent(requireContext(), PremiumSubcriptionActivity::class.java)
                startActivity(intent)
            }
            R.id.tvgetpaidcoins->{
                var intent = Intent(requireContext(), PaidcoinConnectionActivity::class.java)
                startActivity(intent)
            }
        }
    }


}