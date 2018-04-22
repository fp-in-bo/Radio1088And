package com.fpinbo.radio1088.networking

import com.squareup.moshi.Json

data class LastEpisode(
        val response: Response
)

data class Response(val items: List<Item>)

data class Item(
        @Json(name = "episode_id")
        val episodeId: Long,
        @Json(name = "site_url")
        val siteUrl: String
)
