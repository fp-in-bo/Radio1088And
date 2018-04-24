package com.fpinbo.radio1088.inject

import android.arch.lifecycle.ViewModel
import com.fpinbo.radio1088.dispatcher.CommandDispatcher
import com.fpinbo.radio1088.dispatcher.CommandDispatcherImpl
import com.fpinbo.radio1088.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    internal abstract fun bindCommandDispatcher(commandDispatcher: CommandDispatcherImpl): CommandDispatcher
}