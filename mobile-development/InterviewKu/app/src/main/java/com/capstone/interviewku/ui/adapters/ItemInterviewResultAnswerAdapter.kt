package com.capstone.interviewku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.InterviewResultItem
import com.capstone.interviewku.databinding.ItemInterviewResultAnswerBinding

class ItemInterviewResultAnswerAdapter :
    ListAdapter<InterviewResultItem, ItemInterviewResultAnswerAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<InterviewResultItem>() {
            override fun areItemsTheSame(
                oldItem: InterviewResultItem,
                newItem: InterviewResultItem
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: InterviewResultItem,
                newItem: InterviewResultItem
            ): Boolean = oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemInterviewResultAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = getItem(position)

        holder.itemView.context.apply {
            binding.tvItemQuestion.text = getString(
                R.string.question_order_template,
                data.questionOrder,
                itemCount
            )

            data.userAnswer?.let {
                data.score?.let { score ->
                    val scoreValid = if (score.toInt() in 0..5) {
                        score
                    } else {
                        0
                    }

                    binding.ratingBarItemScore.rating = scoreValid.toFloat()
                } ?: run {
                    binding.ratingBarItemScore.isVisible = false
                }

                data.duration?.let { duration ->
                    val minutes = (duration / 60).toInt()
                    val seconds = (duration % 60).toInt()
                    binding.tvItemDuration.text = getString(
                        R.string.duration_template,
                        minutes,
                        seconds
                    )
                } ?: run {
                    binding.ivItemDuration.isVisible = false
                }

                data.retryAttempt?.let { retryAttempt ->
                    if (retryAttempt < 1) {
                        binding.ivItemRetryQuestion.isVisible = false
                    } else {
                        binding.tvItemRetryQuestion.text = getString(
                            R.string.retry_count_template,
                            retryAttempt
                        )
                    }
                } ?: run {
                    binding.ivItemRetryQuestion.isVisible = false
                }

                binding.tvItemAnswer.text = data.userAnswer.ifEmpty { "-" }
                binding.tvItemFeedback.text = data.feedback
            } ?: run {
                binding.tvItemNotAnswered.isVisible = true

                binding.ratingBarItemScore.isVisible = false
                binding.ivItemDuration.isVisible = false
                binding.ivItemRetryQuestion.isVisible = false
                binding.tvItemAnswerTitle.isVisible = false
                binding.tvItemFeedbackTitle.isVisible = false
                binding.tvItemFeedback.isVisible = false
            }
        }
    }

    class ViewHolder(val binding: ItemInterviewResultAnswerBinding) :
        RecyclerView.ViewHolder(binding.root)
}