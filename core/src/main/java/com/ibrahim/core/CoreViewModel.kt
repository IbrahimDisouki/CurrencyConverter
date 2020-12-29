package com.ibrahim.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
abstract class CoreViewModel<I : CoreIntent, R : CoreResult, S : CoreState>(initialState: S) :
    ViewModel() {

    private val intentChannel = Channel<I>(Channel.UNLIMITED)

    protected val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<S>
        get() = mutableState

    init {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                handleIntent(intent)
            }
        }
    }

    suspend infix fun dispatch(intent: I) {
        intentChannel.send(intent)
    }

    abstract suspend fun handleIntent(intent: I)

    protected abstract fun reduce(result: R): S

}