package com.capstone.interviewku.ui.activities.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityLandingBinding
import com.capstone.interviewku.ui.activities.login.LoginActivity
import com.capstone.interviewku.ui.activities.registerbasic.RegisterBasicActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterBasicActivity::class.java))
        }

        binding.root.apply {
            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    when (motionLayout?.currentState) {
                        R.id.first -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.first, R.id.blank)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()

                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.blank, R.id.second)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.second -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.second, R.id.blank)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()

                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.blank, R.id.third)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.third -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.third, R.id.blank)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()

                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.blank, R.id.first)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {
                }
            })

            lifecycleScope.launch {
                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                setTransition(R.id.blank, R.id.first)
                setTransitionDuration(TRANSITION_DURATION)
                transitionToEnd()
            }
        }
    }

    companion object {
        private const val TRANSITION_DURATION = 750
        private const val TRANSITION_DELAY = 500
    }
}