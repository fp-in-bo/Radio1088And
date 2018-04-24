package com.fpinbo.radio1088.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.app.NotificationCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.fpinbo.radio1088.R

private const val notificationId = 1
private const val serviceId = 2

private const val LOG_TAG = "MediaPlaybackService"

class MediaPlaybackService : MediaBrowserServiceCompat(), AudioManager.OnAudioFocusChangeListener {

    companion object {
        const val CUSTOM_ACTION = "CUSTOM_ACTION"
        const val START_STREAMING = "START_STREAMING"
        const val SOURCE_URL = "SOURCE_URL"
        const val TITLE = "TITLE"
        const val IMAGE_URL = "IMAGE_URL"
    }

    private val mediaPlayer by lazy { initMediaPlayer() }
    private val mediaSession by lazy { initMediaSession() }

    private val noisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (mediaPlayer.isPlaying) {
                pause()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerNoisyReceiver()
    }

    private fun initMediaPlayer() = MediaPlayer().apply {
        setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build())
        } else {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        setVolume(1.0f, 1.0f)
        setOnCompletionListener {
            release()
        }
    }

    private fun initMediaSession(): MediaSessionCompat {
        val mediaButtonReceiver = ComponentName(applicationContext, MediaButtonReceiver::class.java)
        return MediaSessionCompat(applicationContext, LOG_TAG, mediaButtonReceiver, null)
            .apply {
                setCallback(MySessionCallback())
                setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
                val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                mediaButtonIntent.setClass(this@MediaPlaybackService, MediaButtonReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this@MediaPlaybackService, 0, mediaButtonIntent, 0)
                setMediaButtonReceiver(pendingIntent)
                this@MediaPlaybackService.sessionToken = sessionToken
            }
    }

    private fun registerNoisyReceiver() {
        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(noisyReceiver, filter)
    }

    private fun hasAudioFocus(): Boolean {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        return result == AudioManager.AUDIOFOCUS_GAIN
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mediaPlayer.setVolume(0.3f, 0.3f)
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                play()
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
        }
    }

    private fun updateMediaSessionState(state: Int) {
        val playbackStateBuilder = PlaybackStateCompat.Builder()
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE)
        } else {
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY)
        }
        playbackStateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        mediaSession.setPlaybackState(playbackStateBuilder.build())
    }

    private fun showPlayingNotification() {
        val builder = MediaStyleNotificationBuilder.from(this, mediaSession)
        builder.addAction(android.support.v4.app.NotificationCompat.Action(R.drawable.ic_pause_accent_24dp, getString(R.string.pause), MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
        builder.setStyle(NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mediaSession.sessionToken))
        builder.setSmallIcon(R.drawable.ic_radio_accent_24dp)
        NotificationManagerCompat.from(this).cancel(notificationId)
        startForeground(serviceId, builder.build())
    }

    private fun showPausedNotification() {
        val builder = MediaStyleNotificationBuilder.from(this, mediaSession)
        builder.addAction(android.support.v4.app.NotificationCompat.Action(R.drawable.ic_play_arrow_accent_24dp, getString(R.string.play), MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
        builder.setStyle(NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mediaSession.sessionToken))
        builder.setSmallIcon(R.drawable.ic_radio_accent_24dp)
        startForeground(serviceId, builder.build())
        stopForeground(true)
        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }

    private fun initMediaSessionMetadata(title: String, imageUrl: String, actionWhenReady: () -> Unit) {

        Glide.with(this).asBitmap().load(imageUrl).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val metadataBuilder = MediaMetadataCompat.Builder()
                metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, resource)
                //lock screen icon for pre lollipop
                metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, resource)

                //Notification icon in card
                metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, BitmapFactory.decodeResource(resources, R.drawable.ic_radio_accent_24dp))
                metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)

                mediaSession.setMetadata(metadataBuilder.build())

                actionWhenReady()
            }
        })
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return MediaBrowserServiceCompat.BrowserRoot(getString(R.string.app_name), null)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val customActions = intent?.getStringExtra(CUSTOM_ACTION)
        when (customActions) {
            START_STREAMING -> {
                startStreaming(intent.getStringExtra(SOURCE_URL), intent.getStringExtra(TITLE), intent.getStringExtra(IMAGE_URL))
            }
        }

        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startStreaming(sourceUrl: String, title: String, imageUrl: String) {
        mediaPlayer.setDataSource(sourceUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            initMediaSessionMetadata(title, imageUrl) {
                play()
            }
        }
    }

    private fun play() {
        if (hasAudioFocus()) {
            mediaSession.isActive = true
            updateMediaSessionState(PlaybackStateCompat.STATE_PLAYING)
            showPlayingNotification()
            mediaPlayer.start()
        }
    }

    private fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            updateMediaSessionState(PlaybackStateCompat.STATE_PAUSED)
            showPausedNotification()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaSession.release()

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.abandonAudioFocus(this)
        unregisterReceiver(noisyReceiver)
        NotificationManagerCompat.from(this).cancel(notificationId)
    }

    inner class MySessionCallback : MediaSessionCompat.Callback() {

        override fun onPlay() {
            super.onPlay()
            play()
        }

        override fun onPause() {
            super.onPause()
            pause()
        }
    }
}