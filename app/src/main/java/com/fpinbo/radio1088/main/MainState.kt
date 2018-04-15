package com.fpinbo.radio1088.main

sealed class MainState {

    object Loading : MainState()
    object Playing : MainState()
    object Stopped : MainState()
}