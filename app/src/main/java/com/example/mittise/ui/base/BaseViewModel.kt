package com.example.mittise.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State> : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state: StateFlow<State?> = _state.asStateFlow()

    protected fun setState(newState: State) {
        viewModelScope.launch {
            _state.emit(newState)
        }
    }

    protected fun updateState(update: (State) -> State) {
        viewModelScope.launch {
            _state.value?.let { currentState ->
                _state.emit(update(currentState))
            }
        }
    }
} 