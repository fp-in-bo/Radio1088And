package com.fpinbo.radio1088.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fpinbo.radio1088.R
import com.fpinbo.radio1088.RadioApplication
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

    override fun onAttach(context: Context) {
        RadioApplication.getAppComponent(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.status.observe(this, Observer {
            it?.handleWith({ handleLoading() }, { handlePlaying() }, { handleStopped() }, { handleError(it.message) })
        })

        start_streaming.setOnClickListener {
            viewModel.startStreaming()
        }
    }

    private fun handleLoading() {
        loading.visibility = View.VISIBLE
        play_pause.visibility = View.GONE
        start_streaming.visibility = View.VISIBLE
        start_streaming.isEnabled = false
    }

    private fun handlePlaying() {
        loading.visibility = View.GONE
        play_pause.visibility = View.VISIBLE
        start_streaming.visibility = View.GONE
        start_streaming.isEnabled = false
        play_pause.setText(R.string.pause)
    }

    private fun handleStopped() {
        loading.visibility = View.GONE
        play_pause.visibility = View.VISIBLE
        start_streaming.visibility = View.GONE
        start_streaming.isEnabled = false
        play_pause.setText(R.string.play)
    }

    private fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
