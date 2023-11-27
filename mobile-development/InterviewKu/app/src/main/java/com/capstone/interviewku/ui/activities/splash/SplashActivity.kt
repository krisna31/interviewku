package com.capstone.interviewku.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.databinding.ActivitySplashBinding
import com.capstone.interviewku.ui.activities.landing.LandingActivity
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(Constants.SPLASH_SCREEN_DELAY)

            startActivity(
                if (viewModel.isLoggedIn()) {
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, LandingActivity::class.java)
                }
            )
            finish()
        }
    }
}