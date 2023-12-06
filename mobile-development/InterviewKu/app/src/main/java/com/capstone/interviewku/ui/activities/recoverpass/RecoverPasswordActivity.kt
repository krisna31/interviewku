package com.capstone.interviewku.ui.activities.recoverpass

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityRecoverPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoverPasswordBinding

    private val viewModel by viewModels<RecoverPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}