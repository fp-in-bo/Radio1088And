package com.fpinbo.radio1088.networking

import com.squareup.moshi.Json

data class LastEpisode(
        @Json(name = "response")
        val response: Response
)

data class Response(val items: List<Item>)

data class Item(
        val episode_id: Long,
        val site_url: String
)
