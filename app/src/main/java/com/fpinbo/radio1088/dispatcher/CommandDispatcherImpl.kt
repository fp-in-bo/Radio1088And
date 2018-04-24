package com.fpinbo.radio1088.dispatcher

import android.content.Context
import android.content.Intent
import com.fpinbo.radio1088.service.MediaPlaybackService
import javax.inject.Inject

class CommandDispatcherImpl @Inject constructor(private val context: Context) : CommandDispatcher {
    override fun startStreaming(streamingInfo: CommandDispatcher.StreamingInfo) {
        val intent = Intent(context, MediaPlaybackService::class.java).apply {
            putExtra(MediaPlaybackService.CUSTOM_ACTION, MediaPlaybackService.START_STREAMING)
            putExtra(MediaPlaybackService.SOURCE_URL, streamingInfo.sourceUrl)
            putExtra(MediaPlaybackService.TITLE, streamingInfo.title)
            putExtra(MediaPlaybackService.IMAGE_URL, streamingInfo.imageUrl)
        }
        context.startService(intent)
    }
}