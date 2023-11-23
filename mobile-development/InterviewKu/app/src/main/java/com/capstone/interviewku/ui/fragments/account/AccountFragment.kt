package com.capstone.interviewku.ui.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.interviewku.databinding.FragmentAccountBinding
import com.capstone.interviewku.ui.ViewModelFactory

class AccountFragment : Fragment() {
    private lateinit var viewModel: AccountViewModel

    private var _binding: FragmentAccountBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        )[AccountViewModel::class.java]
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}