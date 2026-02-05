package com.example.cryptotrade.presentation.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrade.presentation.send.SendScreen
import com.example.cryptotrade.presentation.send.SendViewModel
import com.example.cryptotrade.presentation.send.state.SendSideEffect

@Composable
fun SendScreenRoute(
    viewModel: SendViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SendSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    SendScreen(
        state = state,
        onEvent = viewModel::handleEvent,
    )
}