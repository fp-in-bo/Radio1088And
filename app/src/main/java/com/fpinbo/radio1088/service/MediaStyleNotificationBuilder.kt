package com.fpinbo.radio1088.service

import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.fpinbo.radio1088.PLAYBACK_NOTIFICATION_CHANNEL_ID
import com.fpinbo.radio1088.R

object MediaStyleNotificationBuilder {

    fun idle(context: Context): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, PLAYBACK_NOTIFICATION_CHANNEL_ID)
        builder
            .setContentTitle(context.getString(R.string.app_name))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        return builder
    }

    fun from(context: Context, mediaSession: MediaSessionCompat): NotificationCompat.Builder {
        val controller = mediaSession.controller
        val mediaMetadata = controller.metadata
        val description = mediaMetadata.description

        val builder = NotificationCompat.Builder(context, PLAYBACK_NOTIFICATION_CHANNEL_ID)
        builder
            .setContentTitle(description.title)
            .setContentText(description.subtitle)
            .setSubText(description.description)
            .setLargeIcon(description.iconBitmap)
            .setContentIntent(controller.sessionActivity)
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        return builder
    }
}
