package com.capstone.interviewku.ui.fragments.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentChatbotBinding
import com.capstone.interviewku.ui.activities.chathistory.ChatbotHistoryActivity
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
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}