package com.codechallange.blinqcodechallenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InvitationViewModel (private val invitationRepository: InvitationRepository) : ViewModel() {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    val invitationResponse = MutableLiveData<String>()

    // function to send invitation
    fun sendInvitation(invitationModel: InvitationModel) {
        viewModelScope.launch {
            when (val response = invitationRepository.sendInvitation(invitationModel)) {
                is Result.Success -> {
                    invitationResponse.postValue(response.data)
                }
                is Result.Error -> {
                    if (response.response.code() == 400) {
                        onError("This email address is already in use")
                    }
                }
            }
        }
    }

    // function to set the error message on getting error in response
    private fun onError(message: String) {
        _errorMessage.value = message
    }

}