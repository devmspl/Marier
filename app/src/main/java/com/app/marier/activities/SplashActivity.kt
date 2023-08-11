package com.app.marier.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.app.marier.R
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Utils
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash)
        generateKeyHash(this,"com.app.marier")
        Handler(Looper.getMainLooper()).postDelayed({
            var status: String? = null
            status = CSPreferences.readString(this, Utils.USERLOGIN)
            if (status.equals("true")) {
                val intent2 = Intent(this, HomeActivity::class.java)
                startActivity(intent2)
                finishAffinity()
            } else {
                val intent2 = Intent(this, OnBoardSplashActivity::class.java)
                startActivity(intent2)
                finishAffinity()
            }
        }, 3000)



    }


    fun generateKeyHash(context: Context, packageName: String) {
        try {
            val info = context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), Base64.DEFAULT))
                Log.d("KeyHash", "Hash Key: $hashKey")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }
}