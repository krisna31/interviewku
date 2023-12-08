package com.capstone.interviewku.ui.activities.tipsdetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.R
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.change_password)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}