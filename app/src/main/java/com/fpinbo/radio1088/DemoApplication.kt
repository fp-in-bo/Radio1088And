package com.fpinbo.radio1088

import android.app.Application
import android.content.Context
import com.fpinbo.radio1088.inject.AppComponent
import com.fpinbo.radio1088.inject.AppModule
import com.fpinbo.radio1088.inject.DaggerAppComponent

class DemoApplication : Application() {

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            val app = context.applicationContext as DemoApplication
            return app.appComponent
        }
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}