package com.fpinbo.radio1088.main

import android.arch.lifecycle.ViewModelProvider
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fpinbo.radio1088.R
import com.fpinbo.radio1088.RadioApplication
import com.fpinbo.radio1088.service.MediaPlaybackService
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle(0)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mediaBrowser: MediaBrowserCompat

    private val connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            val token = mediaBrowser.sessionToken
            val mediaController = MediaControllerCompat(context, token)
            mediaController.registerCallback(controllerCallback)
            MediaControllerCompat.setMediaController(activity!!, mediaController)
            buildTransportControls()
            start_streaming.isEnabled = true
            loading.visibility = View.GONE
        }
    }

    private var controllerCallback: MediaControllerCompat.Callback = object : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)

            when (state?.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    showPauseButton()
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    showPlayButton()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        RadioApplication.getAppComponent(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBrowser = MediaBrowserCompat(context, ComponentName(context, MediaPlaybackService::class.java), connectionCallbacks, null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        /*viewModel.status.observe(this, Observer {
            it?.handleWith({ handleLoading() }, { handlePlaying() }, { handleStopped() })
        })*/
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onStop() {
        super.onStop()
        // (see "stay in sync with the MediaSession")
        if (MediaControllerCompat.getMediaController(activity!!) != null) {
            MediaControllerCompat.getMediaController(activity!!).unregisterCallback(controllerCallback)
        }
        mediaBrowser.disconnect()
    }

    fun buildTransportControls() {

        val mediaController = MediaControllerCompat.getMediaController(activity!!)

        start_streaming.setOnClickListener {
            mediaController.transportControls.playFromMediaId("any", null)
            start_streaming.isEnabled = false

        }

        play_pause.setOnClickListener {
            val pbState = mediaController.playbackState.state
            if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.transportControls.pause()
                showPlayButton()
            } else {
                mediaController.transportControls.play()
                showPauseButton()
            }
        }

        // Display the initial state
        val metadata = mediaController.metadata
        val pbState = mediaController.playbackState

        // Register a Callback to stay in sync
        mediaController.registerCallback(controllerCallback)
    }

    private fun showPauseButton() {
        play_pause.visibility = View.VISIBLE
        play_pause.setText(R.string.pause)
    }

    private fun showPlayButton() {
        play_pause.visibility = View.VISIBLE
        play_pause.setText(R.string.play)
    }

    private fun handleLoading() {
        loading.visibility = View.VISIBLE
        play_pause.visibility = View.GONE
    }

    private fun handlePlaying() {
        loading.visibility = View.GONE
        play_pause.visibility = View.VISIBLE
    }

    private fun handleStopped() {
        loading.visibility = View.GONE
        play_pause.visibility = View.VISIBLE
    }
}
