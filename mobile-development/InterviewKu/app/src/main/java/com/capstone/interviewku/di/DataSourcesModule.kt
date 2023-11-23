package com.capstone.interviewku.di

import android.content.Context
import androidx.room.Room
import com.capstone.interviewku.BuildConfig
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.data.room.InterviewKuDatabase
import com.capstone.interviewku.util.Constants
import com.capstone.interviewku.util.Extensions.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourcesModule {
    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context.dataStore)
    }

    @Provides
    fun provideInterviewKuAPIService(): InterviewKuAPIService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitClient = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

        return retrofitClient.create(InterviewKuAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideInterviewKuDatabase(@ApplicationContext context: Context): InterviewKuDatabase {
        return Room.databaseBuilder(
            context,
            InterviewKuDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}