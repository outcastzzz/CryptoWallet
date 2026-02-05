package com.example.cryptotrade.presentation.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrade.presentation.login.LoginScreen
import com.example.cryptotrade.presentation.login.LoginViewModel
import com.example.cryptotrade.presentation.login.state.LoginSideEffect

@Composable
fun LoginScreenRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToWallet: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                LoginSideEffect.NavigateToWallet -> onNavigateToWallet()
            }
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::handleEvent
    )
}