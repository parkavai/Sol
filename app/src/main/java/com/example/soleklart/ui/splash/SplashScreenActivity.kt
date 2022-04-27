package com.example.soleklart.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.soleklart.ui.MainActivity
import com.example.soleklart.databinding.ActivitySplashScreenBinding
import com.example.soleklart.ui.onBoarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            checkForOnboarding()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)   // Delaying 3 seconds
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("BOOLEAN_KEY", true)
        }.apply()
    }

    private fun checkForOnboarding() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("BOOLEAN_KEY", false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            saveData()
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
        }
    }

}