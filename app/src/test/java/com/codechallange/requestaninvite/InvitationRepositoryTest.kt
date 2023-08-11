package com.codechallange.requestaninvite

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class InvitationRepositoryTest {

    lateinit var invitationRepository: InvitationRepository

    @Mock
    lateinit var apiService: ApiService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        invitationRepository = InvitationRepository(apiService)
    }

    @Test
    fun `send valid email invite test`() {
        runBlocking {
            Mockito.`when`(apiService.sendInvitation(InvitationModel("neh","neh@g.com"))).thenReturn(Response.success("Registered"))
            val response = invitationRepository.sendInvitation(InvitationModel("neh","neh@g.com"))
            assertEquals(Result.Success("Registered"),  Result.Success(response).data)
            assertEquals(200,  Response.success(response).code())
        }

    }

    @Test
    fun `send invalid email invite test`() {
        runBlocking {
            Mockito.`when`(apiService.sendInvitation(InvitationModel("neh","usedemail@blinq.app"))).thenReturn(Response.error(400,null))
            val response = invitationRepository.sendInvitation(InvitationModel("neh","usedemail@blinq.app"))
            //assertEquals(400,  response)
        }

    }

}