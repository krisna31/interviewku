package com.capstone.interviewku.ui.activities.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.capstone.interviewku.databinding.ActivityRegisterBinding
import com.capstone.interviewku.ui.fragments.registerbasic.RegisterBasicFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commitNow(allowStateLoss = true) {
            add(binding.frameLayoutRoot.id, RegisterBasicFragment())
        }
    }
}