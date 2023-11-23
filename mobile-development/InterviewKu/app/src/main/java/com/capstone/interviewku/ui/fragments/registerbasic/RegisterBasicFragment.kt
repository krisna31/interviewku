package com.capstone.interviewku.ui.fragments.registerbasic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.interviewku.databinding.FragmentRegisterBasicBinding
import com.capstone.interviewku.ui.ViewModelFactory

class RegisterBasicFragment : Fragment() {
    private lateinit var viewModel: RegisterBasicViewModel

    private var _binding: FragmentRegisterBasicBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        )[RegisterBasicViewModel::class.java]
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}