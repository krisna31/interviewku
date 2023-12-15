package com.capstone.interviewku.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentHomeBinding
import com.capstone.interviewku.ui.activities.interviewresult.InterviewResultActivity
import com.capstone.interviewku.ui.activities.interviewtest.InterviewTestActivity
import com.capstone.interviewku.ui.activities.interviewtrain.InterviewTrainActivity
import com.capstone.interviewku.ui.activities.tipsdetail.TipsDetailActivity
import com.capstone.interviewku.ui.adapters.ItemInterviewPerformanceAdapter
import com.capstone.interviewku.ui.adapters.ItemTipsHomeAdapter
import com.capstone.interviewku.ui.customview.JobFieldChip
import com.capstone.interviewku.util.Constants
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadAllData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        val itemInterviewPerformanceAdapter = ItemInterviewPerformanceAdapter {
            startActivity(
                Intent(context, InterviewResultActivity::class.java).apply {
                    putExtra(InterviewResultActivity.EXTRA_INTERVIEW_ID_KEY, it.interviewId)
                }
            )
        }

        val itemTipsHomeAdapter = ItemTipsHomeAdapter {
            startActivity(
                Intent(context, TipsDetailActivity::class.java).apply {
                    putExtra(
                        TipsDetailActivity.EXTRA_ARTICLE_ENTITY_KEY,
                        Helpers.articleResponseToArticleEntity(it)
                    )
                }
            )
        }

        binding.rvInterviewPerformance.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = itemInterviewPerformanceAdapter
        }

        binding.rvInterviewTips.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = itemTipsHomeAdapter
        }

        binding.btnNoHistoryStartTrain.setOnClickListener {
            startActivity(
                Intent(context, InterviewTrainActivity::class.java)
            )
        }

        binding.btnNoHistoryStartTest.setOnClickListener {
            startActivity(
                Intent(context, InterviewTestActivity::class.java)
            )
        }

        binding.swlMain.setDistanceToTriggerSync(Constants.SWIPE_REFRESH_LAYOUT_TRIGGER_DISTANCE)
        binding.swlMain.setOnRefreshListener {
            binding.swlMain.isRefreshing = false
            viewModel.loadAllData()
        }

        viewModel.allDataState.observe(viewLifecycleOwner) { homeScreenModel ->
            binding.progressBar.isVisible = homeScreenModel is Result.Loading
            binding.swlMain.isEnabled = homeScreenModel !is Result.Loading

            setContentVisibility(false)
            binding.chipgroupInterest.removeAllViews()

            when (homeScreenModel) {
                is Result.Success -> {
                    val userIdentity = homeScreenModel.data.userIdentity
                    val jobFields = homeScreenModel.data.jobFields
                    val interviewPerformance = homeScreenModel.data.interviewPerformance
                    val tipsArticles = homeScreenModel.data.tipsArticles

                    binding.tvGreeting.text = getString(
                        R.string.home_greeting_template,
                        if (userIdentity.lastname == null) {
                            userIdentity.firstname
                        } else {
                            getString(
                                R.string.first_last_name_template,
                                userIdentity.firstname,
                                userIdentity.lastname
                            )
                        }
                    )

                    jobFields.sortedBy {
                        it.name
                    }.forEach { jobFieldItem ->
                        binding.chipgroupInterest.addView(
                            JobFieldChip(context).apply {
                                jobField = jobFieldItem

                                isCheckable = true
                                text = jobFieldItem.name
                            }
                        )
                    }

                    binding.chipgroupInterest.setOnCheckedStateChangeListener { group, _ ->
                        viewModel.setInterestJobField(null)
                        group.children.forEach { v ->
                            val chip = v as JobFieldChip
                            if (chip.isChecked) {
                                chip.jobField?.let { jobField ->
                                    viewModel.setInterestJobField(jobField)
                                }
                            }
                        }
                    }

                    setContentVisibility(true)

                    interviewPerformance.isEmpty().let { isEmpty ->
                        binding.rvInterviewPerformance.isVisible = !isEmpty
                        binding.tvNoHistory.isVisible = isEmpty
                        binding.btnNoHistoryStartTrain.isVisible = isEmpty
                        binding.btnNoHistoryStartTest.isVisible = isEmpty
                    }

                    itemInterviewPerformanceAdapter.submitList(interviewPerformance)
                    itemTipsHomeAdapter.submitList(tipsArticles)
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    context.apply {
                        homeScreenModel.exception
                            .getData()
                            ?.handleHttpException(this)
                            ?.let { message ->
                                Toast.makeText(
                                    this,
                                    getString(R.string.home_load_failed, message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
        }

        viewModel.selectedJobField.observe(viewLifecycleOwner) { jobField ->
            binding.btnInterestStartTrain.apply {
                isEnabled = jobField != null
                jobField?.id?.let { jobFieldId ->
                    setOnClickListener {
                        startActivity(
                            Intent(context, InterviewTrainActivity::class.java).apply {
                                putExtra(InterviewTrainActivity.EXTRA_JOB_FIELD_ID, jobFieldId)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setContentVisibility(isVisible: Boolean) {
        binding.tvGreeting.isVisible = isVisible
        binding.tvWelcome.isVisible = isVisible
        binding.btnInterestStartTrain.isVisible = isVisible
        binding.tvChooseInterestTitle.isVisible = isVisible
        binding.tvPerformanceTitle.isVisible = isVisible
        binding.tvTipsTitle.isVisible = isVisible
        binding.rvInterviewTips.isVisible = isVisible

        if (!isVisible) {
            binding.rvInterviewPerformance.isVisible = false
            binding.tvNoHistory.isVisible = false
            binding.btnNoHistoryStartTrain.isVisible = false
            binding.btnNoHistoryStartTest.isVisible = false
        }
    }
}