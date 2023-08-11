package com.app.marier.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.databinding.FragmentReeferFriendBinding

class ReeferFriendFragment : Fragment(), View.OnClickListener {
private var binding:FragmentReeferFriendBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReeferFriendBinding.inflate(layoutInflater,container,false)
        listeners()
        return binding!!.root

    }

    private fun listeners(){
        binding?.linshare?.setOnClickListener(this)
        binding?.tvclose?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.linshare->{
                val appLink = "https://example.com/app" // Replace with your app link
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, appLink)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
            R.id.tvclose->findNavController().popBackStack()
        }
    }
}