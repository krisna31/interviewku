package com.capstone.interviewku.ui.activities.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
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

        Glide.with(this)
            .load(R.drawable.logo_alt_blue)
            .into(binding.ivLogo)
        Glide.with(this@LandingActivity)
            .load(R.drawable.img_landing_1)
            .into(binding.ivFirst)
        Glide.with(this@LandingActivity)
            .load(R.drawable.img_landing_2)
            .into(binding.ivSecond)
        Glide.with(this@LandingActivity)
            .load(R.drawable.img_landing_3)
            .into(binding.ivThird)

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
                        R.id.blank1 -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.blank1, R.id.first)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.first -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.first, R.id.blank2)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.blank2 -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.blank2, R.id.second)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.second -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.second, R.id.blank3)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.blank3 -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.blank3, R.id.third)
                                setTransitionDuration(TRANSITION_DURATION)
                                transitionToEnd()
                            }
                        }

                        R.id.third -> {
                            lifecycleScope.launch {
                                delay((TRANSITION_DELAY + TRANSITION_DURATION).toLong())
                                setTransition(R.id.third, R.id.blank1)
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
                setTransition(R.id.blank1, R.id.first)
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