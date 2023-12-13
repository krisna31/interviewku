package com.capstone.interviewku.ui.activities.chathistory

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityChatbotHistoryBinding
import com.capstone.interviewku.ui.adapters.ItemChatbotHistoryAdapter
import com.capstone.interviewku.ui.fragments.chat.ChatbotFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatbotHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotHistoryBinding

    private val viewModel by viewModels<ChatbotHistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatbotHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.chatbot_history)
        }

        val chatHistoryAdapter = ItemChatbotHistoryAdapter { chat ->
            startActivity(Intent(this, ChatbotFragment::class.java))
        }.also { adapter ->
            adapter.addLoadStateListener { combinedLoadStates ->
                binding.progressBar.isVisible =
                    combinedLoadStates.refresh == LoadState.Loading
                            || combinedLoadStates.append == LoadState.Loading

                binding.tvNoItem.isVisible =
                    combinedLoadStates.refresh != LoadState.Loading
                            && adapter.itemCount == 0
            }
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@ChatbotHistoryActivity)
            adapter = chatHistoryAdapter
        }

        viewModel.chatHistory.observe(this) {
            chatHistoryAdapter.submitData(lifecycle, it)
        }

    }
}