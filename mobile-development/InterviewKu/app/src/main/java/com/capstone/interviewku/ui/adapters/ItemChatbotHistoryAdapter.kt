package com.capstone.interviewku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.Chat
import com.capstone.interviewku.databinding.ItemChatbotHistoryBinding
import com.capstone.interviewku.util.Helpers

class ItemChatbotHistoryAdapter : PagingDataAdapter<Chat, ItemChatbotHistoryAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(
            oldItem: Chat,
            newItem: Chat
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Chat,
            newItem: Chat
        ): Boolean = oldItem == newItem
    }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        ItemChatbotHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        getItem(position)?.let { data ->
            binding.apply {
                tvItemQuestion.text = data.question
                tvItemAnswer.text = data.answer

                tvDate.text = tvDate.context.getString(
                    R.string.started_at_template,
                    Helpers.tzTimeStringToDate(data.createdAt)?.let { date ->
                        Helpers.dateToIndonesianFormat(date)
                    } ?: run {
                        "-"
                    }
                )
            }
        }
    }

    class ViewHolder(val binding: ItemChatbotHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}

