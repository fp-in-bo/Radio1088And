package com.fpinbo.radio1088.main

import javax.inject.Inject

class MainServiceImpl @Inject constructor() : MainService {
    override fun giveMeSomeString() = "Hello from main injected service"
}