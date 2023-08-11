package com.codechallange.requestaninvite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InvitationViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(InvitationViewModel::class.java)) {
            InvitationViewModel(invitationRepository = InvitationRepository(
                apiService = ApiService.getInstance()
            )) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}