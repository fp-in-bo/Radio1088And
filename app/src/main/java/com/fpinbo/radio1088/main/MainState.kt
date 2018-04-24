package com.fpinbo.radio1088.main

sealed class MainState {

    object Loading : MainState()
    object Playing : MainState()
    object Stopped : MainState()
    data class Error(val message: String) : MainState()

    fun <T> handleWith(
        loading: (Loading) -> T,
        playing: (Playing) -> T,
        stopped: (Stopped) -> T,
        error: (Error) -> T): T {

        return when (this) {
            is MainState.Loading -> loading(this)
            is MainState.Playing -> playing(this)
            is MainState.Stopped -> stopped(this)
            is MainState.Error -> error(this)
        }
    }
}