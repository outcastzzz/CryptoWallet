package com.example.cryptotrade.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val readyChanges: Flow<Boolean>
    val token: String

    suspend fun sendOtp(email: String)

    suspend fun verifyOtp(code: String)

    suspend fun logout()
}