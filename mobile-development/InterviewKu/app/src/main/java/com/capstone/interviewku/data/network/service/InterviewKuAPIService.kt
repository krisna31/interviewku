package com.capstone.interviewku.data.network.service

import com.capstone.interviewku.data.network.response.BaseResponse
import com.capstone.interviewku.data.network.response.InterviewAnswerSubmitResponse
import com.capstone.interviewku.data.network.response.InterviewAnswersResponse
import com.capstone.interviewku.data.network.response.InterviewQuestionsResponse
import com.capstone.interviewku.data.network.response.InterviewResultResponse
import com.capstone.interviewku.data.network.response.JobFieldsResponse
import com.capstone.interviewku.data.network.response.JobPositionsResponse
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.data.network.response.RefreshTokenResponse
import com.capstone.interviewku.data.network.response.UserDetailResponse
import com.capstone.interviewku.data.network.response.UserIdentityModifyResponse
import com.capstone.interviewku.data.network.response.UserIdentityResponse
import com.capstone.interviewku.data.network.response.UserRegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface InterviewKuAPIService {
    // answers
    @GET("/answers/{questionId}")
    suspend fun getInterviewAnswer(
        @Header("Authorization") bearerToken: String,
        @Path("questionId") questionId: String
    ): InterviewAnswersResponse

    // authentications
    @POST("/authentications")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @HTTP(method = "DELETE", path = "/authentications", hasBody = true)
    @FormUrlEncoded
    suspend fun logout(
        @Field("refreshToken") refreshToken: String,
    ): BaseResponse

    @PUT("/authentications")
    @FormUrlEncoded
    suspend fun refreshAccessToken(
        @Field("refreshToken") refreshToken: String,
    ): RefreshTokenResponse

    // change-password
    @PUT("/change-password")
    @FormUrlEncoded
    suspend fun changePassword(
        @Header("Authorization") bearerToken: String,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
    ): BaseResponse

    // interview
    @GET("/interview/questions")
    suspend fun startInterviewSession(
        @Header("Authorization") bearerToken: String,
        @Query("mode") mode: String
    ): InterviewQuestionsResponse

    @GET("/interview/{interviewId}/questions")
    suspend fun getCurrentInterviewQuestions(
        @Header("Authorization") bearerToken: String,
        @Path("interviewId") interviewId: String,
    ): InterviewQuestionsResponse

    @POST("/interview/{interviewId}/answers")
    @Multipart
    suspend fun sendInterviewAnswer(
        @Part audio: MultipartBody.Part,
        @Part("jobFieldName") jobFieldName: RequestBody,
        @Part("jobPositionName") jobPositionName: RequestBody,
        @Part("retryAttempt") retryAttempt: RequestBody,
        @Part("question") question: RequestBody,
        @Part("questionOrder") questionOrder: RequestBody,
    ): InterviewAnswerSubmitResponse

    @PUT("/interview/{interviewId}")
    @FormUrlEncoded
    suspend fun endInterviewSession(
        @Header("Authorization") bearerToken: String,
        @Path("interviewId") interviewId: String,
        @Field("completed") isCompleted: Boolean,
    ): InterviewResultResponse

    @GET("/interview/{interviewId}")
    suspend fun getInterviewResult(
        @Header("Authorization") bearerToken: String,
        @Path("interviewId") interviewId: String,
    ): InterviewResultResponse

    // job
    @GET("/job/fields")
    suspend fun getJobFields(
        @Header("Authorization") bearerToken: String,
    ): JobFieldsResponse

    @GET("/job/positions")
    suspend fun getJobPositions(
        @Header("Authorization") bearerToken: String,
    ): JobPositionsResponse

    // users
    @POST("/users")
    @FormUrlEncoded
    suspend fun register(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserRegisterResponse

    @GET("/users")
    @FormUrlEncoded
    suspend fun getUser(
        @Header("Authorization") bearerToken: String,
    ): UserDetailResponse

    @GET("/users/identity")
    suspend fun getUserIdentity(
        @Header("Authorization") bearerToken: String,
    ): UserIdentityResponse

    @POST("/users/identity")
    @FormUrlEncoded
    suspend fun addUserIdentity(
        @Header("Authorization") bearerToken: String,
        @Field("jobPositionId") jobPositionId: Int,
        @Field("gender") gender: String,
        @Field("dateBirth") dateBirth: String,
        @Field("currentCity") currentCity: String
    ): UserIdentityModifyResponse

    @PUT("/users/identity")
    @FormUrlEncoded
    suspend fun editUserIdentity(
        @Header("Authorization") bearerToken: String,
        @Field("jobPositionId") jobPositionId: Int,
        @Field("gender") gender: String,
        @Field("dateBirth") dateBirth: String,
        @Field("currentCity") currentCity: String
    ): UserIdentityModifyResponse
}