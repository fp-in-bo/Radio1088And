package com.fpinbo.radio1088.network

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface SpreakerService {
    @GET("users/8573356/episodes?limit=1")
    fun lastEpisode(): Single<Response<LastEpisode>>

    companion object {

        operator fun invoke(): SpreakerService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build())
                )
                .baseUrl("https://api.spreaker.com/v2/")
                .build()

            return retrofit.create(SpreakerService::class.java)
        }
    }
}