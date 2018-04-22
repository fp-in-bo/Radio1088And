package com.fpinbo.radio1088.networking

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface SpreakerService {
    @GET("users/8573356/episodes?limit=1")
    fun lastEpisode(): Single<LastEpisode>

    companion object {
        fun create(): SpreakerService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .baseUrl("https://api.spreaker.com/v2/")
                    .build()

            return retrofit.create(SpreakerService::class.java)
        }
    }
}

