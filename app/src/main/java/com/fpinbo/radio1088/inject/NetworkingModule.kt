package com.fpinbo.radio1088.inject

import com.fpinbo.radio1088.networking.SpreakerService
import dagger.Module
import dagger.Provides

@Module
class NetworkingModule {
    @Provides
    fun createSpreakerService() = SpreakerService.create()
}