package com.fpinbo.radio1088.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.fpinbo.radio1088.dispatcher.CommandDispatcher
import com.fpinbo.radio1088.network.SpreakerService
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val commandDispatcher: CommandDispatcher,
    private val spreakerService: SpreakerService
) : ViewModel() {

    private val mutableStatus: MutableLiveData<MainState> = MutableLiveData()

    private var disposable: Disposable? = null

    val status: LiveData<MainState>
        get() = mutableStatus

    fun startStreaming() {
        mutableStatus.value = MainState.Loading

        spreakerService.lastEpisode()
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->

                if (response.isSuccessful) {
                    mutableStatus.postValue(MainState.Playing)
                    val episode = response.body()!!.response.items.lastOrNull()

                    episode?.let {
                        val episodeId = it.episodeId
                        val episodeUrl = "https://api.spreaker.com/v2/episodes/$episodeId/play"
                        commandDispatcher.startStreaming(CommandDispatcher.StreamingInfo(episodeUrl, it.title, it.imageUrl))
                    }
                } else {
                    response?.errorBody()?.string().notifyError()
                }

            }, { throwable ->
                throwable.message.notifyError()
            })

        //TODO retrieve latest episode
        //TODO register as listener of service, so that we can update status and update the UI
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    private fun String?.notifyError() {
        this?.let {
            mutableStatus.postValue(MainState.Error(it))
        }
    }
}