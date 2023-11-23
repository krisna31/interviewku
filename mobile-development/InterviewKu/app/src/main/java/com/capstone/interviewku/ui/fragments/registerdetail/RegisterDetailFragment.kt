package com.capstone.interviewku.ui.fragments.registerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.interviewku.databinding.FragmentRegisterDetailBinding
import com.capstone.interviewku.ui.ViewModelFactory

class RegisterDetailFragment : Fragment() {
    private lateinit var viewModel: RegisterDetailViewModel

    private var _binding: FragmentRegisterDetailBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        )[RegisterDetailViewModel::class.java]
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}