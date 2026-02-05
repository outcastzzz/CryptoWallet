package com.example.cryptotrade.presentation.splash

import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.domain.repository.AuthRepository
import com.example.cryptotrade.presentation.base.BaseViewModel
import com.example.cryptotrade.presentation.splash.state.SplashSideEffect
import com.example.cryptotrade.presentation.splash.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<SplashState, SplashSideEffect>(SplashState()) {

    private val didRoute = AtomicBoolean(false)

    init {
        observeAuth()
    }

    private fun observeAuth() {
        viewModelScope.launch {
            authRepository.readyChanges.first { changed -> changed }

            if (didRoute.getAndSet(true)) return@launch

            val token = authRepository.token

            if (token.isNotEmpty()) {
                sendEffect(SplashSideEffect.NavigateToWallet)
            } else {
                sendEffect(SplashSideEffect.NavigateToEmail)
            }
        }
    }
}