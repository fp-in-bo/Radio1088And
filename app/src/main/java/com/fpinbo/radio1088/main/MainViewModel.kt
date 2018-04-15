package com.fpinbo.radio1088.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.media.MediaPlayer
import com.fpinbo.radio1088.R
import javax.inject.Inject

private val fileRes = R.raw.la_tigre_di_carta_puntata_1

class MainViewModel @Inject constructor(
    private val context: Context) : ViewModel() {

    private val mediaPlayer by lazy { MediaPlayer() }

    private val mutableLiveData: MutableLiveData<MainState> = MutableLiveData()

    fun start() {

        if (!mediaPlayer.isPlaying) {
            mutableLiveData.value = MainState.Loading
            val afd = context.resources.openRawResourceFd(fileRes)
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                mutableLiveData.value = MainState.Playing
            }
        }
    }

    val status: LiveData<MainState>
        get() = mutableLiveData

    fun togglePlayStatus() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    override fun onCleared() {
        mediaPlayer.release()
        super.onCleared()
    }
}