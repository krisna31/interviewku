package com.capstone.interviewku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.InterviewHistoryData
import com.capstone.interviewku.data.network.types.InterviewMode
import com.capstone.interviewku.databinding.ItemInterviewPerformanceBinding
import com.capstone.interviewku.util.Helpers

class ItemInterviewPerformanceAdapter(
    private val onItemClick: (InterviewHistoryData) -> Unit
) : ListAdapter<InterviewHistoryData, ItemInterviewPerformanceAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<InterviewHistoryData>() {
        override fun areItemsTheSame(
            oldItem: InterviewHistoryData,
            newItem: InterviewHistoryData
        ): Boolean = oldItem.interviewId == newItem.interviewId

        override fun areContentsTheSame(
            oldItem: InterviewHistoryData,
            newItem: InterviewHistoryData
        ): Boolean = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemInterviewPerformanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = getItem(position)

        holder.itemView.context.apply {
            binding.tvMode.text = getString(
                R.string.mode_template,
                when (data.mode) {
                    InterviewMode.TRAIN.mode -> {
                        getString(R.string.interview_train)
                    }

                    InterviewMode.TEST.mode -> {
                        getString(R.string.interview_test)
                    }

                    else -> {
                        ""
                    }
                }
            )
            binding.tvJobField.text = getString(R.string.job_field_template, data.jobFieldName)

            if (data.completed) {
                data.score?.let {
                    val score = if (it in 1..5) {
                        it
                    } else {
                        1
                    }

                    binding.ratingBarScore.rating = score.toFloat()
                    binding.tvScoreDescription.text =
                        resources.getStringArray(R.array.rating_summary)[score - 1]
                }
            } else {
                binding.ratingBarScore.isVisible = false
                binding.tvScoreDescription.text = getString(R.string.interview_not_finished)
            }

            binding.tvDate.text = getString(
                R.string.started_at_template,
                Helpers.tzTimeStringToDate(data.startedAt)?.let { date ->
                    Helpers.dateToIndonesianFormat(date)
                } ?: run {
                    "-"
                }
            )

            binding.root.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    class ViewHolder(val binding: ItemInterviewPerformanceBinding) :
        RecyclerView.ViewHolder(binding.root)
}