package com.fpinbo.radio1088.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.fpinbo.radio1088.dispatcher.CommandDispatcher
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val commandDispatcher: CommandDispatcher
) : ViewModel() {

    private val mutableStatus: MutableLiveData<MainState> = MutableLiveData()

    fun start() {
        //TODO retrieve latest episode
        //TODO register as listener of service, so that we can update status and update the UI

        mutableStatus.value = MainState.Ready
    }

    val status: LiveData<MainState>
        get() = mutableStatus

    fun startStreaming() {
        commandDispatcher.startStreaming()
    }
}