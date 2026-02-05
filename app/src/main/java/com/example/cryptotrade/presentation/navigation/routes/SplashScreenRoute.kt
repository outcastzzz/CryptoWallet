package com.example.cryptotrade.presentation.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.cryptotrade.presentation.splash.SplashScreen
import com.example.cryptotrade.presentation.splash.SplashViewModel
import com.example.cryptotrade.presentation.splash.state.SplashSideEffect

@Composable
fun SplashScreenRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToWallet: () -> Unit,
    onNavigateToEmail: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SplashSideEffect.NavigateToWallet -> onNavigateToWallet()
                SplashSideEffect.NavigateToEmail -> onNavigateToEmail()
            }
        }
    }

    SplashScreen()
}