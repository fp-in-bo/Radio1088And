package com.fpinbo.radio1088.dispatcher

import android.content.Context
import android.content.Intent
import com.fpinbo.radio1088.service.MediaPlaybackService
import javax.inject.Inject

class CommandDispatcherImpl @Inject constructor(private val context: Context) : CommandDispatcher {

    override fun startStreaming() {
        val intent = Intent(context, MediaPlaybackService::class.java).apply {
            putExtra(MediaPlaybackService.CUSTOM_ACTION, MediaPlaybackService.START_STREAMING)
            //TODO more extras like url and metadata
        }
        context.startService(intent)
    }
}