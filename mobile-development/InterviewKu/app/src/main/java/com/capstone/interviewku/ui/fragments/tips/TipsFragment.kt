package com.capstone.interviewku.ui.fragments.tips

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentTipsBinding
import com.capstone.interviewku.ui.activities.tipsdetail.TipsDetailActivity
import com.capstone.interviewku.ui.adapters.ItemTipsScreenAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TipsFragment : Fragment() {
    private var _binding: FragmentTipsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<TipsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        val tipsScreenAdapter = ItemTipsScreenAdapter {
            startActivity(Intent(activity, TipsDetailActivity::class.java).apply {
                putExtra(TipsDetailActivity.EXTRA_ARTICLE_ENTITY_KEY, it)
            })
        }.also { adapter ->
            adapter.addLoadStateListener { combinedLoadStates ->
                binding.progressBar.isVisible =
                    combinedLoadStates.refresh == LoadState.Loading
                            || combinedLoadStates.append == LoadState.Loading

                if (combinedLoadStates.refresh is LoadState.Error) {
                    Toast.makeText(
                        activity,
                        getString(
                            R.string.fetch_new_data_failed,
                            (combinedLoadStates.refresh as LoadState.Error).error.message ?: ""
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (combinedLoadStates.append is LoadState.Error) {
                    Toast.makeText(
                        activity,
                        getString(
                            R.string.fetch_new_data_failed,
                            (combinedLoadStates.append as LoadState.Error).error.message ?: ""
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.rvInterviewTips.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = tipsScreenAdapter
        }

        viewModel.articles.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                tipsScreenAdapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}