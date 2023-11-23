package com.capstone.interviewku.ui.activities.tipsdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityTipsDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TipsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipsDetailBinding

    private val viewModel by viewModels<TipsDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTipsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}