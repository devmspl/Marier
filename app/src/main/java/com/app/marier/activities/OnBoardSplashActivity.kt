package com.app.marier.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.viewpager.widget.ViewPager
import com.app.marier.R
import com.app.marier.adapter.OnBoardSplashPagerAdapter
import com.app.marier.databinding.ActivityOnBoardSplashBinding
import com.app.marier.staticmodel.OnBoardSwipeAdapterModel
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class OnBoardSplashActivity : AppCompatActivity() {
    private var binding: ActivityOnBoardSplashBinding? = null
    private var currentPosition = 0
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardSplashBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        initViewPager()

//        movetonextscreen()
    }

    private fun initViewPager() {
        val arrayList: ArrayList<OnBoardSwipeAdapterModel> = ArrayList()
        arrayList.add(
            OnBoardSwipeAdapterModel(
                "Discover people",
                R.drawable.onboardfirstscreen,
                "Find like minded people to \n connect with"
            )
        )
        arrayList.add(
            OnBoardSwipeAdapterModel(
                "Match with them",
                R.drawable.onboard2screeen,
                "See who like you and who \n likes you and connect with them"
            )
        )
        arrayList.add(
            OnBoardSwipeAdapterModel(
                "Chat with them",
                R.drawable.onboardscreen3s,
                "Chat with your favourite people\n who you connected with"
            )
        )
        val onBoardSplashPagerAdapter = OnBoardSplashPagerAdapter(this, arrayList,binding?.viewpager)
        binding?.viewpager?.adapter = onBoardSplashPagerAdapter

        // Move to the next screen automatically when the last page of the ViewPager is reached
        binding?.viewpager?.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == onBoardSplashPagerAdapter.count - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent2 = Intent(this@OnBoardSplashActivity, SignupActivity::class.java)
                        startActivity(intent2)
                        finish()
                    }, 2000)
                }
            }
        })

        // Start the timer to automatically swipe the ViewPager
//        val DELAY = 3000L
//        val timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                mHandler.post {
//                    binding?.viewpager?.let { viewPager ->
//                        if (viewPager.currentItem < onBoardSplashPagerAdapter.count - 1) {
//                            viewPager.currentItem += 1
//                        } else {
//                            timer.cancel()
//                        }
//                    }
//                }
//            }
//        }, DELAY, DELAY)
    }

    override fun onDestroy() {
        val timer = Timer()
        super.onDestroy()
        timer.cancel()
    }


}