package com.capstone.interviewku.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.TipsRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.di.Injection
import com.capstone.interviewku.ui.activities.changepassword.ChangePasswordViewModel
import com.capstone.interviewku.ui.activities.interviewhistory.InterviewHistoryViewModel
import com.capstone.interviewku.ui.activities.interviewresult.InterviewResultViewModel
import com.capstone.interviewku.ui.activities.interviewtest.InterviewTestViewModel
import com.capstone.interviewku.ui.activities.interviewtrain.InterviewTrainViewModel
import com.capstone.interviewku.ui.activities.login.LoginViewModel
import com.capstone.interviewku.ui.activities.profile.ProfileViewModel
import com.capstone.interviewku.ui.activities.splash.SplashViewModel
import com.capstone.interviewku.ui.activities.tipsdetail.TipsDetailViewModel
import com.capstone.interviewku.ui.fragments.account.AccountViewModel
import com.capstone.interviewku.ui.fragments.home.HomeViewModel
import com.capstone.interviewku.ui.fragments.registerbasic.RegisterBasicViewModel
import com.capstone.interviewku.ui.fragments.registerdetail.RegisterDetailViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val interviewRepository: InterviewRepository,
    private val jobRepository: JobRepository,
    private val tipsRepository: TipsRepository,
    private val userRepository: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> ChangePasswordViewModel(
                authRepository,
            )

            modelClass.isAssignableFrom(InterviewHistoryViewModel::class.java) -> InterviewHistoryViewModel(
                interviewRepository,
            )

            modelClass.isAssignableFrom(InterviewResultViewModel::class.java) -> InterviewResultViewModel(
                interviewRepository,
            )

            modelClass.isAssignableFrom(InterviewTestViewModel::class.java) -> InterviewTestViewModel(
                jobRepository,
                interviewRepository,
            )

            modelClass.isAssignableFrom(InterviewTrainViewModel::class.java) -> InterviewTrainViewModel(
                jobRepository,
                interviewRepository,
            )

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                authRepository,
            )

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                jobRepository,
                userRepository,
            )

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(
                authRepository,
            )

            modelClass.isAssignableFrom(TipsDetailViewModel::class.java) -> TipsDetailViewModel(
                tipsRepository,
            )

            modelClass.isAssignableFrom(AccountViewModel::class.java) -> AccountViewModel(
                authRepository,
                userRepository,
            )

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                userRepository,
                tipsRepository,
            )

            modelClass.isAssignableFrom(RegisterBasicViewModel::class.java) -> RegisterBasicViewModel(
                authRepository,
                userRepository,
            )

            modelClass.isAssignableFrom(RegisterDetailViewModel::class.java) -> RegisterDetailViewModel(
                jobRepository,
                userRepository,
            )

            else -> throw IllegalArgumentException()
        } as T
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideInterviewRepository(context),
                Injection.provideJobRepository(context),
                Injection.provideTipsRepository(context),
                Injection.provideUserRepository(context),
            ).also {
                instance = it
            }
        }
    }
}