package com.capstone.interviewku.ui.activities.chathistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityChatbotHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatbotHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotHistoryBinding

    private val viewModel by viewModels<ChatbotHistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatbotHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}