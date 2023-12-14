package com.capstone.interviewku.ui.fragments.chat

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
import com.capstone.interviewku.data.network.response.Chat
import com.capstone.interviewku.databinding.FragmentChatbotBinding
import com.capstone.interviewku.ui.activities.chathistory.ChatbotHistoryActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Result
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

        val context = requireContext()

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
            val userQuestion = binding.etQuestion.text.toString()
            viewModel.sendQuestion(userQuestion)

            binding.etQuestion.isEnabled = false
            it.isEnabled = false
        }

        viewModel.sendQuestionState.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = result is Result.Loading

            when (result) {
                is Result.Success -> {
                    binding.etQuestion.isEnabled = true
                    binding.btnSubmitQuestion.isEnabled = true
                    binding.etQuestion.setText("")

                    setResultText(result.data.data)
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    binding.etQuestion.isEnabled = true
                    binding.btnSubmitQuestion.isEnabled = true
                    Toast.makeText(
                        context,
                        result.exception.getData()?.handleHttpException(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        setResultText(null)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setResultText(chat: Chat?) {
        binding.tvResultTitle.isVisible = chat != null
        binding.tvResultQuestionTitle.isVisible = chat != null
        binding.tvResultAnswerTitle.isVisible = chat != null

        chat?.let {
            binding.tvResultQuestion.text = it.question
            binding.tvResultAnswer.text = it.answer
        }
    }
}