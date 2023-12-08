package com.capstone.interviewku.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.InterviewHistoryData
import com.capstone.interviewku.data.network.types.InterviewMode
import com.capstone.interviewku.databinding.ItemInterviewHistoryBinding
import com.capstone.interviewku.util.Helpers
import java.text.DateFormat

class ItemInterviewHistoryAdapter(
    private val onItemClick: (InterviewHistoryData) -> Unit
) :
    PagingDataAdapter<InterviewHistoryData, ItemInterviewHistoryAdapter.ViewHolder>(
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        ItemInterviewHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        holder.itemView.context.apply {
            getItem(position)?.let { data ->
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

                data.score?.let {
                    val score = if (it in 0..5) {
                        it
                    } else {
                        0
                    }

                    binding.ratingBarScore.rating = score.toFloat()
                    binding.tvScoreDescription.text =
                        resources.getStringArray(R.array.rating_summary)[score]
                } ?: run {
                    binding.ratingBarScore.isVisible = false
                    binding.tvScoreDescription.text = getString(R.string.interview_not_finished)
                }

                binding.tvJobField.text = getString(R.string.job_field_template, data.jobFieldName)
                binding.tvDate.text = getString(
                    R.string.started_at_template,
                    Helpers.tzTimeStringToDate(data.startedAt)?.also {
                        DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.DEFAULT)
                            .format(it)
                    } ?: run {
                        "-"
                    }
                )

                binding.root.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    class ViewHolder(val binding: ItemInterviewHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}