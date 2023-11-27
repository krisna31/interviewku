package com.capstone.interviewku.ui.fragments.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.interviewku.databinding.FragmentTipsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TipsFragment : Fragment() {
    private var _binding: FragmentTipsBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<TipsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
            }
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}