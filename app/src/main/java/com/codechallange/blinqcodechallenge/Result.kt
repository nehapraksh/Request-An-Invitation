package com.codechallange.blinqcodechallenge

import retrofit2.Response

/*
*
* class to wrap API result
* */
sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error<T>(val response: Response<T>): Result<T>()
}
