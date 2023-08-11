package com.codechallange.requestaninvite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class InvitationViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    lateinit var invitationViewModel: InvitationViewModel

    lateinit var invitationRepository: InvitationRepository

    @Mock
    lateinit var apiService: ApiService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        invitationRepository = InvitationRepository(apiService)
        invitationViewModel = InvitationViewModel(invitationRepository)
    }

    @After
    fun setUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun `call the sendInvitation() method with valid email`() {
        runBlocking{
            invitationViewModel.sendInvitation(InvitationModel("neh","neh@g.com"))
            invitationViewModel.invitationResponse.observeForever{
                assertEquals("Registered", it)
            }

        }
    }

    @Test
    fun `call the sendInvitation() method with invalid email`() {
        runBlocking{
            invitationViewModel.sendInvitation(InvitationModel("neh","usedemail@blinq.app"))
            invitationViewModel.errorMessage.observeForever{
                assertEquals("This email address is already in use", it)
            }

        }
    }

}