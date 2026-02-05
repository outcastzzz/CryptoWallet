package com.example.cryptotrade.presentation.navigation.routes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrade.presentation.wallet.WalletScreen
import com.example.cryptotrade.presentation.wallet.WalletViewModel
import com.example.cryptotrade.presentation.wallet.state.WalletSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreenRoute(
    viewModel: WalletViewModel = hiltViewModel(),
    onNavigateToSend: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                WalletSideEffect.NavigateToSend -> onNavigateToSend()
                WalletSideEffect.NavigateToLogin -> onNavigateToLogin()
                WalletSideEffect.ShowSnackBar -> snackBarHostState.showSnackbar(
                    message = "Copied",
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }

    WalletScreen(
        state = state,
        onEvent = viewModel::handleEvent,
        snackBarHostState = snackBarHostState,
        pullToRefreshState = pullToRefreshState,
    )
}
