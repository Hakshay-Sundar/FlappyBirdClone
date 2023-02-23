package com.example.flappybirdclone.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.flappybirdclone.R

class Background(
    private val resources: Resources,
    private val screenHeight: Int
): Sprite {

    private var backgroundSky: Bitmap
    private var backgroundGround: Bitmap

    init {
        val skyHeight = resources.getDimension(R.dimen.sky_height).toInt()
        val groundHeight = resources.getDimension(R.dimen.ground_height).toInt()

        val skyBmp = BitmapFactory.decodeResource(resources, R.drawable.sky)
        backgroundSky = Bitmap.createScaledBitmap(skyBmp, skyBmp.width, skyHeight, false)

        val groundBmp = BitmapFactory.decodeResource(resources, R.drawable.ground)
        backgroundGround = Bitmap.createScaledBitmap(groundBmp, groundBmp.width, groundHeight, false)

    }
    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(backgroundSky, 0.0f, 0.0f, null)
        canvas.drawBitmap(backgroundSky, backgroundSky.width.toFloat(), 0.0f, null)
        canvas.drawBitmap(backgroundGround, 0.0f, (screenHeight - backgroundGround.height).toFloat(), null)
        canvas.drawBitmap(backgroundGround, backgroundGround.width.toFloat(), (screenHeight - backgroundGround.height).toFloat(), null)
    }

    override fun update() {
        // stub
    }
}