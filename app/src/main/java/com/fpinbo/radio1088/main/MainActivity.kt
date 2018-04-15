package com.fpinbo.radio1088.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.danielecampogiani.demo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }
    }
}
