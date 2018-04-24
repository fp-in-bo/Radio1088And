package com.fpinbo.radio1088.main

sealed class MainState {

    object Loading : MainState()
    object Ready : MainState()
    object Playing : MainState()
    object Stopped : MainState()

    fun <T> handleWith(loading: (Loading) -> T, ready: (Ready) -> T, playing: (Playing) -> T, stopped: (Stopped) -> T): T {

        return when (this) {
            is MainState.Loading -> loading(this)
            is MainState.Ready -> ready(this)
            is MainState.Playing -> playing(this)
            is MainState.Stopped -> stopped(this)
        }
    }
}