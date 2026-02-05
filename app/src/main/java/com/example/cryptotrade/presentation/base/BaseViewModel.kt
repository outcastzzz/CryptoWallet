package com.example.cryptotrade.presentation.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * base class for every ViewModel
 *
 * @param S - type of UI state
 * @param E - type of single events (navigation, system dialog, etc.)
 */
abstract class BaseViewModel<S, E>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected val currentState: S
        get() = _state.value

    private val _sideEffect = Channel<E>()
    val sideEffect: Flow<E> = _sideEffect.receiveAsFlow()

    protected fun updateState(reducer: S.() -> S) {
        _state.update { oldState -> oldState.reducer() }
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }

    protected suspend inline fun <T> safeCall(
        tag: String,
        block: suspend () -> T
    ): T? = runCatching { block() }
        .onFailure { Log.d("LOG_ERROR", "$tag error: ${it.message}") }
        .getOrNull()
}