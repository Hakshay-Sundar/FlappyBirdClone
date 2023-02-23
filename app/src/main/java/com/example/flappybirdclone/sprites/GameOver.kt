package com.example.flappybirdclone.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.flappybirdclone.R

class GameOver constructor(
    private val resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
) : Sprite {

    private var gameOver: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.gameover)

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(
            gameOver,
            (screenWidth / 2 - gameOver.width / 2).toFloat(),
            (screenHeight / 2 - gameOver.height / 2).toFloat(),
            null
        )
    }

    override fun update() {
        // stub
    }
}