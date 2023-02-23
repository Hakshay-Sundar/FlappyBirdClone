package com.example.flappybirdclone.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.flappybirdclone.R

class GameMessage constructor(
    private val resources: Resources, private val screenHeight: Int, private val screenWidth: Int
) : Sprite {

    private var gameMessage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.message)

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(
            gameMessage,
            (screenWidth / 2 - gameMessage.width / 2).toFloat(),
            (screenHeight / 2 - gameMessage.height / 4).toFloat(),
            null
        )
    }

    override fun update() {
        // stub
    }
}