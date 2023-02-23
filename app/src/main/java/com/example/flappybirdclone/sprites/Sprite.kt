package com.example.flappybirdclone.sprites

import android.graphics.Canvas

interface Sprite {
    fun draw(canvas: Canvas)
    fun update()
}