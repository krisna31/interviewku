package com.capstone.interviewku.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivitySplashBinding
import com.capstone.interviewku.ui.activities.landing.LandingActivity
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.ui.activities.registerdetail.RegisterDetailActivity
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
            Glide.with(this@SplashActivity)
                .load(R.drawable.logo_alt_2_blue)
                .into(binding.civLogo)

            delay(Constants.SPLASH_SCREEN_DELAY)

            startActivity(
                if (viewModel.isLoggedIn()) {
                    if (viewModel.isHasUserIdentity()) {
                        Intent(this@SplashActivity, MainActivity::class.java)
                    } else {
                        Intent(this@SplashActivity, RegisterDetailActivity::class.java)
                    }
                } else {
                    Intent(this@SplashActivity, LandingActivity::class.java)
                }
            )
            finish()
        }
    }
}