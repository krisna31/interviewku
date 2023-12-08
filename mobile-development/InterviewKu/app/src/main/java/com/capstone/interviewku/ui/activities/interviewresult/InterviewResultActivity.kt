package com.capstone.interviewku.ui.activities.interviewresult

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.types.InterviewMode
import com.capstone.interviewku.databinding.ActivityInterviewResultBinding
import com.capstone.interviewku.ui.adapters.ItemInterviewResultAnswerAdapter
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat

@AndroidEntryPoint
class InterviewResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewResultBinding

    private val viewModel by viewModels<InterviewResultViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.interview_result)
        }

        intent.getStringExtra(INTERVIEW_ID_KEY)?.let { interviewId ->
            val interviewResultAnswerAdapter = ItemInterviewResultAnswerAdapter()

            binding.rvDetail.apply {
                layoutManager = LinearLayoutManager(
                    this@InterviewResultActivity,
                    RecyclerView.HORIZONTAL,
                    false
                )
                adapter = interviewResultAnswerAdapter
            }

            viewModel.getInterviewResultById(interviewId)
            viewModel.interviewResultState.observe(this) {
                binding.clContent.isVisible = it is Result.Success
                binding.progressBar.isVisible = it is Result.Loading

                when (it) {
                    is Result.Success -> {
                        it.data.data?.let { data ->
                            interviewResultAnswerAdapter.submitList(data.answers)

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
                            binding.tvJobField.text = getString(
                                R.string.job_field_template,
                                data.jobFieldName
                            )
                            binding.tvStartTime.text = getString(
                                R.string.started_at_template,
                                Helpers.tzTimeStringToDate(data.startedAt)?.also { date ->
                                    DateFormat.getDateTimeInstance(
                                        DateFormat.FULL,
                                        DateFormat.DEFAULT
                                    ).format(date)
                                } ?: run {
                                    "-"
                                }
                            )
                            binding.tvDuration.text = getString(
                                R.string.duration_label_template,
                                if (data.completed && data.totalDuration != null) {
                                    val duration = data.totalDuration.toInt()
                                    val minutes = duration / 60
                                    val seconds = duration % 60

                                    getString(
                                        R.string.duration_template,
                                        minutes,
                                        seconds
                                    )
                                } else {
                                    getString(R.string.interview_not_finished)
                                }
                            )

                            if (data.completed) {
                                data.score?.let { score ->
                                    val scoreValid = if (score.toInt() in 0..5) {
                                        score.toInt()
                                    } else {
                                        0
                                    }

                                    binding.ratingBarScore.rating = scoreValid.toFloat()
                                    binding.tvRatingSummary.text =
                                        resources.getStringArray(R.array.rating_summary)[scoreValid]
                                }

                                binding.tvFeedback.text = data.feedback
                            } else {
                                binding.tvNotFinished.isVisible = true
                                binding.ratingBarScore.isVisible = false
                                binding.tvFeedbackTitle.isVisible = false

                                binding.tvRatingSummary.text =
                                    getString(R.string.interview_not_finished)
                            }
                        }
                    }

                    is Result.Loading -> {}

                    is Result.Error -> {
                        it.exception.getData()?.handleHttpException(this)
                    }
                }
            }
        } ?: run {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val INTERVIEW_ID_KEY = "INTERVIEW_ID_KEY"
    }
}