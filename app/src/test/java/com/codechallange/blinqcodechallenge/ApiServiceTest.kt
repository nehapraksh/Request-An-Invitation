package com.codechallange.blinqcodechallenge

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = GsonBuilder()
            .setLenient()
            .create()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService::class.java)
    }


    @Test
    fun `send invitation api test`() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("Registered"))
            val response = apiService.sendInvitation(InvitationModel("neh","neh@g.com"))
            val request = mockWebServer.takeRequest()
            assertEquals("/fakeAuth",request.path)
            assertEquals(true, response.body()?.isEmpty() == false)
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}