package com.fpinbo.radio1088.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainService: MainService) : ViewModel() {

    private val mutableLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        mutableLiveData.value = mainService.giveMeSomeString()
    }

    val liveData: LiveData<String>
        get() = mutableLiveData
}