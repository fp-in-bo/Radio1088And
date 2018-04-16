package com.fpinbo.radio1088.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private val mutableLiveData: MutableLiveData<MainState> = MutableLiveData()

    fun start() {

        /*if (!mediaPlayer.isPlaying) {
            mutableLiveData.value = MainState.Loading
            mediaPlayer.setDataSource("https://api.spreaker.com/v2/episodes/14524218/play")
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                mutableLiveData.value = MainState.Playing
            }
        }*/
    }

    val status: LiveData<MainState>
        get() = mutableLiveData

    fun togglePlayStatus() {
        /*if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mutableLiveData.value = MainState.Stopped
        } else {
            mediaPlayer.start()
            mutableLiveData.value = MainState.Playing
        }
    }*/
    }
}