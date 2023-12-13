package com.capstone.interviewku.ui.fragments.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.ChatbotDetailResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.databinding.FragmentChatbotBinding
import com.capstone.interviewku.ui.activities.chathistory.ChatbotHistoryActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatbotFragment : Fragment() {
    private var _binding: FragmentChatbotBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<ChatbotViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_fragment_chatbot)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_fragment_chatbot_history -> {
                        startActivity(Intent(context, ChatbotHistoryActivity::class.java))
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }

        binding.btnSubmitQuestion.setOnClickListener {
            binding.btnSubmitQuestion.isEnabled = false
            val userQuestion = binding.etQuestion.text.toString()
            viewModel.sendQuestion(userQuestion)
        }

        viewModel.sendQuestionState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true

                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    binding.btnSubmitQuestion.isEnabled = true
                    binding.etQuestion.text?.clear()
                    updateUIWithResult(result.data)

                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    binding.btnSubmitQuestion.isEnabled = false
                    Toast.makeText(context, "Error: ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun updateUIWithResult(result: ChatbotDetailResponse?) {
        binding.tvResultQuestion.text = result?.data?.question.toString()
        binding.tvResultAnswer.text = result?.data?.answer.toString()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}