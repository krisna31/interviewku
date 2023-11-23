package com.capstone.interviewku.ui.activities.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}