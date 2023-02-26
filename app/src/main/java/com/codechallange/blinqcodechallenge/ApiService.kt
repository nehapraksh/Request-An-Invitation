package com.codechallange.blinqcodechallenge

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {

    /**
     * POST fakeAuth - send invitation
     * @param invitationModel
     * @return success
     */
    @POST("fakeAuth")
    suspend fun sendInvitation(@Body invitationModel: InvitationModel): Response<String>

    companion object {
        private const val API_URL = "https://us-central1-blinkapp-684c1.cloudfunctions.net/"
        private var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        var retrofitService: ApiService? = null
        fun getInstance(): ApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.addInterceptor(interceptor()).build())
                    .build()
                retrofitService = retrofit.create(ApiService::class.java)
            }
            return retrofitService!!
        }

        private fun interceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

        var gson = GsonBuilder()
            .setLenient()
            .create()
    }
}