package com.example.flappybirdclone

import android.app.Activity
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Bundle
import android.view.Window

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.main_activity)
    }
}
