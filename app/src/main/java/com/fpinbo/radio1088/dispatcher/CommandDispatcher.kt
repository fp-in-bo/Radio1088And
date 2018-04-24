package com.fpinbo.radio1088.dispatcher

interface CommandDispatcher {

    data class StreamingInfo(
        val sourceUrl: String,
        val title: String,
        val imageUrl: String
    )

    fun startStreaming(streamingInfo: StreamingInfo)
}