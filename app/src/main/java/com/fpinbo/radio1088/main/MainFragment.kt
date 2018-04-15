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
import com.fpinbo.radio1088.DemoApplication
import com.fpinbo.radio1088.R
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle(0)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        DemoApplication.getAppComponent(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        play_pause.setOnClickListener {
            viewModel.togglePlayStatus()
        }
        viewModel.start()

        viewModel.status.observe(this, Observer {
            it?.handleWith({ handleLoading() }, { handlePlaying() }, { handleStopped() })
        })
    }

    private fun handleLoading() {
        loading.visibility = View.VISIBLE
        play_pause.visibility = View.GONE
    }

    private fun handlePlaying() {
        loading.visibility = View.GONE
        play_pause.visibility = View.VISIBLE
        play_pause.setImageResource(R.drawable.ic_pause_circle_outline_accent_24dp)
    }

    private fun handleStopped() {
        loading.visibility = View.GONE
        play_pause.visibility = View.VISIBLE
        play_pause.setImageResource(R.drawable.ic_play_circle_outline_accent_24dp)
    }
}
