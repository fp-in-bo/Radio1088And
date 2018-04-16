package com.fpinbo.radio1088

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.fpinbo.radio1088.inject.AppComponent
import com.fpinbo.radio1088.inject.AppModule
import com.fpinbo.radio1088.inject.DaggerAppComponent

class RadioApplication : Application() {

    companion object {

        fun getAppComponent(context: Context): AppComponent {
            val app = context.applicationContext as RadioApplication
            return app.appComponent
        }
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        registerNotificationChannels()
    }

    private fun registerNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = getString(R.string.playback_notification_channel)
        val importance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(PLAYBACK_NOTIFICATION_CHANNEL_ID, channelName, importance)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}