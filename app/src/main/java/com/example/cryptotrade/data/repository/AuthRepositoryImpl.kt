package com.example.cryptotrade.data.repository

import com.example.cryptotrade.data.web3.DynamicSdkWrapper
import com.example.cryptotrade.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val sdkWrapper: DynamicSdkWrapper,
) : AuthRepository {

    override val readyChanges: Flow<Boolean>
        get() = sdkWrapper.readyChanges

    override val token: String
        get() = sdkWrapper.token

    override suspend fun sendOtp(email: String) {
        sdkWrapper.sendOtp(email = email)
    }

    override suspend fun verifyOtp(code: String) {
        sdkWrapper.verifyOtp(code = code)
    }

    override suspend fun logout() {
        sdkWrapper.logout()
    }
}