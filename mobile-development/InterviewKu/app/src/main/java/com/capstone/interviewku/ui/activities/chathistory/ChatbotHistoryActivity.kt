package com.capstone.interviewku.ui.activities.chathistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityChatbotHistoryBinding
import com.capstone.interviewku.ui.adapters.ItemChatbotHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatbotHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotHistoryBinding
    private lateinit var chatbotHistoryAdapter: ItemChatbotHistoryAdapter

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

        chatbotHistoryAdapter = ItemChatbotHistoryAdapter().also { adapter ->
            adapter.addLoadStateListener { combinedLoadStates ->
                binding.progressBar.isVisible =
                    combinedLoadStates.refresh == LoadState.Loading
                            || combinedLoadStates.append == LoadState.Loading

                binding.tvNoItem.isVisible =
                    combinedLoadStates.refresh is LoadState.NotLoading
                            && adapter.itemCount == 0

                if (combinedLoadStates.refresh is LoadState.Error) {
                    showErrorDialog(
                        (combinedLoadStates.refresh as LoadState.Error).error.message
                            ?: getString(R.string.reload_instruction)
                    )
                }

                if (combinedLoadStates.append is LoadState.Error) {
                    showErrorDialog(
                        (combinedLoadStates.append as LoadState.Error).error.message
                            ?: getString(R.string.reload_instruction)
                    )
                }
            }
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@ChatbotHistoryActivity)
            adapter = chatbotHistoryAdapter
        }

        viewModel.chatHistory.observe(this) {
            chatbotHistoryAdapter.submitData(lifecycle, it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun showErrorDialog(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.error_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                chatbotHistoryAdapter.refresh()
            }
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                finish()
            }
            .create()

        if (!isFinishing) {
            alertDialog.show()
        }
    }
}