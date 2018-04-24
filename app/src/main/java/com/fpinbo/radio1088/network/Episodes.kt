package com.fpinbo.radio1088.network

import com.squareup.moshi.Json

data class LastEpisode(
    val response: Response
)

data class Response(val items: List<Item>)

data class Item(
    @Json(name = "episode_id")
    val episodeId: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "image_original_url")
    val imageUrl: String
)