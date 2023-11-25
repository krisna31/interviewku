package com.capstone.interviewku.ui.fragments.registerbasic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.fragment.app.viewModels
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentRegisterBasicBinding
import com.capstone.interviewku.ui.fragments.registerdetail.RegisterDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterBasicFragment : Fragment() {
    private var _binding: FragmentRegisterBasicBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<RegisterBasicViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            parentFragmentManager.commitNow(allowStateLoss = true) {
                replace(R.id.frame_layout_root, RegisterDetailFragment())
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}