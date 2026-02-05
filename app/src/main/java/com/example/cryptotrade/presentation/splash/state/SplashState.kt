package com.example.cryptotrade.presentation.splash.state

data class SplashState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false
)

sealed interface SplashSideEffect {
    data object NavigateToWallet : SplashSideEffect
    data object NavigateToEmail : SplashSideEffect
}
