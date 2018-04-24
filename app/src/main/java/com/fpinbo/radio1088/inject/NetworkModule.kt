package com.fpinbo.radio1088.inject

import com.fpinbo.radio1088.network.SpreakerService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesSpreakerService() = SpreakerService()
}