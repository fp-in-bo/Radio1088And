package com.fpinbo.radio1088.inject

import com.fpinbo.radio1088.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelBuilder::class,
    MainModule::class,
    NetworkingModule::class
])
interface AppComponent {

    fun inject(mainFragment: MainFragment)
}