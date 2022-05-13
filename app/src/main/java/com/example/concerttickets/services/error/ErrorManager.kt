package com.example.concerttickets.services.error

import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorManager @Inject constructor(private val moshi: Moshi) {

    private val itemNotFound = "ItemNotFound"
    private val userNotFound = "UserNotFound"
    private val accessTokenRequired = "AccessTokenRequired"
    private val badAccessToken = "BadAccessToken"

    fun getAppError(errorJson: String?) {
        val adapter = moshi.adapter(ErrorResponse::class.java)
        if (errorJson == null) throw UnknownErrorException()
        val errorResponse = adapter.fromJson(errorJson)
        errorResponse?.let {
            it.errorCode?.let { errorCode ->
                throw when (errorCode) {
                    itemNotFound -> ItemNotFoundException()
                    userNotFound -> UserNotFoundException()
                    accessTokenRequired -> AccessTokenRequiredException()
                    badAccessToken -> BadAccessTokenException()
                    else -> UnknownException()
                }
            }
        }
    }
}