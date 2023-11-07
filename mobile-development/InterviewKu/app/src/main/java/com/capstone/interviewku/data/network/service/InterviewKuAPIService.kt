package com.capstone.interviewku.data.network.service

import com.capstone.interviewku.data.network.response.BaseResponse
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.data.network.response.RefreshTokenResponse
import com.capstone.interviewku.data.network.response.UserDetailResponse
import com.capstone.interviewku.data.network.response.UserRegisterResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InterviewKuAPIService {
    @POST("/authentications")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginResponse

    @DELETE("/authentications")
    @FormUrlEncoded
    suspend fun logout(
        @Field("refreshToken") refreshToken: String,
    ): BaseResponse

    @PUT("/authentications")
    @FormUrlEncoded
    suspend fun refreshAccessToken(
        @Field("refreshToken") refreshToken: String,
    ): RefreshTokenResponse

    @POST("/users")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("fullname") fullname: String,
    ): UserRegisterResponse

    @GET("/users/{id}")
    suspend fun getUserDetail(
        @Path("id") id: String
    ): UserDetailResponse
}