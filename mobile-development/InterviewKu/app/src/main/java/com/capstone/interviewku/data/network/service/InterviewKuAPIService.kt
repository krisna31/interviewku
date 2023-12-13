package com.capstone.interviewku.data.network.service

import com.capstone.interviewku.data.network.response.ArticleDetailResponse
import com.capstone.interviewku.data.network.response.ArticlesResponse
import com.capstone.interviewku.data.network.response.BaseResponse
import com.capstone.interviewku.data.network.response.ChatbotDetailResponse
import com.capstone.interviewku.data.network.response.ChatbotResponse
import com.capstone.interviewku.data.network.response.InterviewAnswerSubmitResponse
import com.capstone.interviewku.data.network.response.InterviewHistoryResponse
import com.capstone.interviewku.data.network.response.InterviewQuestionsResponse
import com.capstone.interviewku.data.network.response.InterviewResultResponse
import com.capstone.interviewku.data.network.response.JobFieldsResponse
import com.capstone.interviewku.data.network.response.JobPositionsResponse
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.data.network.response.RefreshTokenResponse
import com.capstone.interviewku.data.network.response.UserDetailResponse
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
    // articles
    @GET("/articles")
    suspend fun getArticles(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ArticlesResponse

    @GET("/articles/{articleId}")
    suspend fun getArticleById(
        @Header("Authorization") bearerToken: String,
        @Path("articleId") articleId: Int,
    ): ArticleDetailResponse

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

    // chats
    @GET("/chats")
    suspend fun getChatHistory(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ChatbotResponse

    @POST("/chats")
    @FormUrlEncoded
    suspend fun sendQuestion(
        @Header("Authorization") bearerToken: String,
        @Field("question") question: String,
    ): ChatbotDetailResponse

    @GET("/chats/{chatId}")
    suspend fun getChatById(
        @Header("Authorization") bearerToken: String,
        @Path("chatId") chatId: String,
    ): ChatbotDetailResponse

    // interview
    @GET("/interviews")
    suspend fun getAllInterviewResults(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): InterviewHistoryResponse

    @GET("/interviews/questions")
    suspend fun startInterviewSession(
        @Header("Authorization") bearerToken: String,
        @Query("mode") mode: String,
        @Query("jobFieldId") jobFieldId: Int,
    ): InterviewQuestionsResponse

    @POST("/interviews/{interviewId}/answers")
    @Multipart
    suspend fun sendInterviewAnswer(
        @Header("Authorization") bearerToken: String,
        @Path("interviewId") interviewId: String,
        @Part("token") token: RequestBody,
        @Part audio: MultipartBody.Part,
        @Part("retryAttempt") retryAttempt: RequestBody,
        @Part("question") question: RequestBody,
        @Part("questionOrder") questionOrder: RequestBody,
    ): InterviewAnswerSubmitResponse

    @PUT("/interviews/{interviewId}")
    @FormUrlEncoded
    suspend fun endInterviewSession(
        @Header("Authorization") bearerToken: String,
        @Path("interviewId") interviewId: String,
        @Field("completed") isCompleted: Boolean,
        @Field("token") token: String
    ): InterviewResultResponse

    @GET("/interviews/{interviewId}")
    suspend fun getInterviewResultById(
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

    // reset-password
    @POST("/reset-password")
    @FormUrlEncoded
    suspend fun requestPasswordReset(
        @Field("email") email: String,
    ): BaseResponse

    @POST("/reset-password/verify")
    @FormUrlEncoded
    suspend fun verifyPasswordReset(
        @Field("email") email: String,
        @Field("otp") otpCode: String,
    ): BaseResponse

    @PUT("/reset-password")
    @FormUrlEncoded
    suspend fun recoverPassword(
        @Field("email") email: String,
        @Field("newPassword") newPassword: String,
    ): BaseResponse

    // users
    @POST("/users")
    @FormUrlEncoded
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String?,
    ): UserRegisterResponse

    @GET("/users")
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
    ): UserIdentityResponse

    @PUT("/users/identity")
    @FormUrlEncoded
    suspend fun editUserIdentity(
        @Header("Authorization") bearerToken: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String?,
        @Field("jobPositionId") jobPositionId: Int,
        @Field("gender") gender: String,
        @Field("dateBirth") dateBirth: String,
        @Field("currentCity") currentCity: String
    ): UserIdentityResponse
}