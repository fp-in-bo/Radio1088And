package com.fpinbo.radio1088.networking

import com.squareup.moshi.Moshi
import org.junit.Test

class LastEpisodeTest {
    @Test
    fun parseResponse() {
        val episodeId: Long = 1
        val siteUrl = "url"

        val json = "{\"response\":{\"items\":[" +
                "{\"episode_id\":$episodeId," +
                "\"type\":\"type\"," +
                "\"title\":\"title\"," +
                "\"duration\":3518240,\"explicit\":false,\"show_id\":1,\"author_id\":1," +
                "\"site_url\":\"$siteUrl\"," +
                "\"image_url\":\"url\"," +
                "\"image_original_url\":\"url\"," +
                "\"published_at\":\"2018-04-21 17:00:05\"," +
                "\"download_enabled\":true," +
                "\"waveform_url\":\"url\"}" +
                "]," +
                "\"next_url\":\"url\"}}"

        val sut = Moshi.Builder().build()
                .adapter(LastEpisode::class.java)

        val actual = sut.fromJson(json)
        val item = actual?.response?.items?.last()

        assert(item?.episode_id == episodeId)
        assert(item?.site_url == siteUrl)
    }
}