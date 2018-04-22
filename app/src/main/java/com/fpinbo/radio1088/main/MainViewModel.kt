package com.fpinbo.radio1088.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.media.MediaPlayer
import com.fpinbo.radio1088.networking.LastEpisode
import com.fpinbo.radio1088.networking.SpreakerService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val context: Context,
        private val spreakerService: SpreakerService) : ViewModel() {

    private val mediaPlayer by lazy { MediaPlayer() }

    private val mutableLiveData: MutableLiveData<MainState> = MutableLiveData()

    private var disposable: Disposable? = null

    fun start() {
        if (!mediaPlayer.isPlaying) {
            disposable = spreakerService.lastEpisode()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { response: LastEpisode ->
                startMediaPlayerForEpisode(response.response.items.last().episode_id)
            }
        }
    }

    private fun startMediaPlayerForEpisode(episodeId: Long) {
        mutableLiveData.value = MainState.Loading
        mediaPlayer.setDataSource("https://api.spreaker.com/v2/episodes/$episodeId/play")
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            mutableLiveData.value = MainState.Playing
        }
    }

    val status: LiveData<MainState>
        get() = mutableLiveData

    fun togglePlayStatus() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mutableLiveData.value = MainState.Stopped
        } else {
            mediaPlayer.start()
            mutableLiveData.value = MainState.Playing
        }
    }

    override fun onCleared() {
        disposable?.dispose()
        mediaPlayer.release()
        super.onCleared()
    }
}