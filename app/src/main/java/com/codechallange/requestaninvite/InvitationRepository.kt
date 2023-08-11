package com.codechallange.requestaninvite

/*
* repository class for sending invitation logic
* */
class InvitationRepository (private val apiService: ApiService){

    suspend fun sendInvitation(body: InvitationModel) : Result<String> {
        val response = apiService.sendInvitation(body)
        return if (response.isSuccessful && response.code() == 200) {
            val responseBody = response.body()
            if (responseBody != null ) {
                Result.Success(responseBody)
            } else {
                Result.Error(response)
            }
        } else {
            Result.Error(response)
        }
    }

}