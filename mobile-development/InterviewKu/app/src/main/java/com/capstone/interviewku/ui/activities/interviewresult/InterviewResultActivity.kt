package com.capstone.interviewku.ui.activities.interviewresult

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.InterviewResultData
import com.capstone.interviewku.data.network.types.InterviewMode
import com.capstone.interviewku.databinding.ActivityInterviewResultBinding
import com.capstone.interviewku.ui.activities.interviewtest.InterviewTestActivity
import com.capstone.interviewku.ui.activities.interviewtrain.InterviewTrainActivity
import com.capstone.interviewku.ui.adapters.ItemInterviewResultAnswerAdapter
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

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

        if (intent.hasExtra(EXTRA_INTERVIEW_RESULT_KEY)) {
            val interviewResultData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    EXTRA_INTERVIEW_RESULT_KEY,
                    InterviewResultData::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_INTERVIEW_RESULT_KEY)
            }

            interviewResultData?.let {
                viewModel.setInterviewResult(it)
            } ?: run {
                finish()
            }
        } else if (intent.hasExtra(EXTRA_INTERVIEW_ID_KEY)) {
            intent.getStringExtra(EXTRA_INTERVIEW_ID_KEY)?.let { interviewId ->
                viewModel.setInterviewResultId(interviewId)
                viewModel.getInterviewResultById()
            } ?: run {
                finish()
            }
        } else {
            finish()
        }

        val interviewResultAnswerAdapter = ItemInterviewResultAnswerAdapter()

        binding.rvDetail.apply {
            layoutManager = LinearLayoutManager(
                this@InterviewResultActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = interviewResultAnswerAdapter
        }

        viewModel.interviewResultState.observe(this) {
            binding.clContent.isVisible = it is Result.Success
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    val data = it.data

                    interviewResultAnswerAdapter.submitList(data.answers.sortedBy { item ->
                        item.questionOrder
                    })

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
                        Helpers.tzTimeStringToDate(data.startedAt)?.let { date ->
                            Helpers.dateToIndonesianFormat(date)
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
                            val scoreValid = if (score.toInt() in 1..5) {
                                score.toInt()
                            } else {
                                0
                            }

                            binding.ratingBarScore.rating = scoreValid.toFloat()
                            binding.tvRatingSummary.text =
                                resources.getStringArray(R.array.rating_summary)[scoreValid - 1]
                        }

                        binding.tvFeedback.text = data.feedback
                    } else {
                        binding.tvNotFinished.isVisible = true
                        binding.ratingBarScore.isVisible = false
                        binding.tvFeedbackTitle.isVisible = false

                        binding.tvRatingSummary.text =
                            getString(R.string.interview_not_finished)
                    }

                    binding.btnNextStep.apply {
                        text = data.score?.let { score ->
                            if (score >= 3) {
                                when (data.mode) {
                                    InterviewMode.TRAIN.mode -> {
                                        context.getString(R.string.continue_to_test)
                                    }

                                    InterviewMode.TEST.mode -> {
                                        context.getString(R.string.start_other_session)
                                    }

                                    else -> {
                                        getString(R.string.try_again)
                                    }
                                }
                            } else {
                                getString(R.string.try_again)
                            }
                        } ?: run {
                            getString(R.string.try_again)
                        }

                        setOnClickListener {
                            startActivity(
                                data.score?.let { score ->
                                    if (score >= 3) {
                                        when (data.mode) {
                                            InterviewMode.TRAIN.mode -> {
                                                Intent(
                                                    this@InterviewResultActivity,
                                                    InterviewTestActivity::class.java
                                                )
                                            }

                                            InterviewMode.TEST.mode -> {
                                                Intent(
                                                    this@InterviewResultActivity,
                                                    InterviewTestActivity::class.java
                                                )
                                            }

                                            else -> {
                                                Intent(
                                                    this@InterviewResultActivity,
                                                    InterviewTrainActivity::class.java
                                                )
                                            }
                                        }
                                    } else {
                                        Intent(
                                            this@InterviewResultActivity,
                                            InterviewTrainActivity::class.java
                                        )
                                    }
                                } ?: run {
                                    Intent(
                                        this@InterviewResultActivity,
                                        InterviewTrainActivity::class.java
                                    )
                                }
                            )
                        }
                    }
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(it.exception.peekData().handleHttpException(this))
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.getInterviewResultById()
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
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_INTERVIEW_ID_KEY = "EXTRA_INTERVIEW_ID_KEY"
        const val EXTRA_INTERVIEW_RESULT_KEY = "EXTRA_INTERVIEW_RESULT_KEY"
    }
}