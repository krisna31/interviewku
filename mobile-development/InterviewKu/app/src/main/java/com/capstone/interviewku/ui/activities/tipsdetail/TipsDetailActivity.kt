package com.capstone.interviewku.ui.activities.tipsdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityTipsDetailBinding
import com.capstone.interviewku.ui.ViewModelFactory

class TipsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTipsDetailBinding

    private val viewModel by viewModels<TipsDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTipsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}