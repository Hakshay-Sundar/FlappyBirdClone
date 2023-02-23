package com.example.flappybirdclone.sprites

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.flappybirdclone.GameManagerCallback
import com.example.flappybirdclone.R

class Bird constructor(
    private val resources: Resources,
    private val screenHeight: Int,
    private val gameManagerCallback: GameManagerCallback
) : Sprite {

    private var birdDown: Bitmap
    private var birdUp: Bitmap
    private var birdX: Float = resources.getDimension(R.dimen.bird_x_pos)
    private var birdY: Float = (screenHeight / 3).toFloat()
    private var birdMovementState by mutableStateOf(BirdMovement())
    private val birdWidth = resources.getDimension(R.dimen.bird_width).toInt()
    private val birdHeight = resources.getDimension(R.dimen.bird_height).toInt()

    private var hasCollided by mutableStateOf(false)

    init {
        //Instantiate Bird movement state
        birdMovementState = birdMovementState.copy(
            gravity = resources.getDimension(R.dimen.bird_gravity),
            flappyBoost = resources.getDimension(R.dimen.bird_boost)
        )

        //Down Bird
        val birdDownBmp = BitmapFactory.decodeResource(resources, R.drawable.bird_down)
        birdDown = Bitmap.createScaledBitmap(birdDownBmp, birdWidth, birdHeight, false)

        // Up Bird
        val birdUpBmp = BitmapFactory.decodeResource(resources, R.drawable.bird_up)
        birdUp = Bitmap.createScaledBitmap(birdUpBmp, birdWidth, birdHeight, false)
    }

    override fun draw(canvas: Canvas) {
        if (birdMovementState.currentFallingSpeed < 0) {
            canvas.drawBitmap(
                birdUp, birdX, birdY, null
            )
        } else {
            canvas.drawBitmap(birdDown, birdX, birdY, null)

        }
    }

    override fun update() {
        if (hasCollided) {
            if ((birdY + birdDown.height) < screenHeight) {
                birdY += birdMovementState.currentFallingSpeed
                birdMovementState = birdMovementState.copy(
                    currentFallingSpeed = birdMovementState.currentFallingSpeed + birdMovementState.gravity
                )
            }
        } else {
            birdY += birdMovementState.currentFallingSpeed
            birdMovementState = birdMovementState.copy(
                currentFallingSpeed = birdMovementState.currentFallingSpeed + birdMovementState.gravity
            )
            gameManagerCallback.updateBirdPos(
                birdPos = Rect(
                    birdX.toInt(),
                    birdY.toInt(),
                    (birdX + birdWidth).toInt(),
                    (birdY + birdHeight).toInt()
                )
            )
        }
    }

    fun onTouch() {
        if (!hasCollided) {
            birdMovementState = birdMovementState.copy(
                currentFallingSpeed = birdMovementState.flappyBoost
            )
        }
    }

    fun collision() {
        hasCollided = true
    }

    fun reset() {
        birdX = resources.getDimension(R.dimen.bird_x_pos)
        birdY = (screenHeight / 3).toFloat()
        hasCollided = false
        birdMovementState = birdMovementState.copy(
            gravity = resources.getDimension(R.dimen.bird_gravity),
            currentFallingSpeed = 0.0f,
            flappyBoost = resources.getDimension(R.dimen.bird_boost)
        )
        gameManagerCallback.updateBirdPos(
            birdPos = Rect(
                birdX.toInt(),
                birdY.toInt(),
                (birdX + birdWidth).toInt(),
                (birdY + birdHeight).toInt()
            )
        )
    }
}


data class BirdMovement(
    val gravity: Float = 0.0f,
    val currentFallingSpeed: Float = 0.0f,
    val flappyBoost: Float = 0.0f,
)