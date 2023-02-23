package com.example.flappybirdclone.sprites

import android.content.res.Resources
import android.graphics.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.flappybirdclone.ObstacleCallBack
import com.example.flappybirdclone.R
import kotlin.random.Random

class Obstacle constructor(
    private val resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
    private val callback: ObstacleCallBack
) : Sprite {

    private var pipeState by mutableStateOf(PipeObstacleState())
    private var pipeImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pipes)

    init {
        val random = Random(System.currentTimeMillis())
        val minHeightLower = resources.getDimension(R.dimen.obstacle_min_height_lower).toInt()
        val obstacleSeparation = resources.getDimension(R.dimen.obstacle_separation).toInt()

        pipeState = pipeState.copy(
            speed = resources.getDimension(R.dimen.obstacle_speed).toInt(),
            separation = obstacleSeparation,
            pipeHeadHeight = resources.getDimension(R.dimen.obstacle_head_height).toInt(),
            extraPipeHeadWidth = resources.getDimension(R.dimen.obstacle_extra_head_width).toInt(),
            height = random.nextInt(screenHeight - (2 * minHeightLower) - obstacleSeparation) + minHeightLower,
            width = resources.getDimension(R.dimen.obstacle_width).toInt(),
            xPos = screenWidth
        )
    }

    override fun draw(canvas: Canvas) {
        val lowerPipe = Rect(
            pipeState.xPos,
            screenHeight - pipeState.height,
            pipeState.xPos + pipeState.width,
            screenHeight
        )
        val lowerPipeHead = Rect(
            pipeState.xPos - pipeState.extraPipeHeadWidth,
            screenHeight - pipeState.height - pipeState.pipeHeadHeight,
            pipeState.xPos + pipeState.width + pipeState.extraPipeHeadWidth,
            screenHeight - pipeState.height
        )

        val upperPipe = Rect(
            pipeState.xPos,
            0,
            pipeState.xPos + pipeState.width,
            screenHeight - pipeState.height - pipeState.separation - (2 * pipeState.pipeHeadHeight)
        )
        val upperPipeHead = Rect(
            pipeState.xPos - pipeState.extraPipeHeadWidth,
            screenHeight - pipeState.height - pipeState.separation - (2 * pipeState.pipeHeadHeight),
            pipeState.xPos + pipeState.width + pipeState.extraPipeHeadWidth,
            screenHeight - pipeState.height - pipeState.separation - pipeState.pipeHeadHeight
        )

        val paint = Paint()
        canvas.drawBitmap(pipeImage, null, lowerPipe, paint)
        canvas.drawBitmap(pipeImage, null, lowerPipeHead, paint)
        canvas.drawBitmap(pipeImage, null, upperPipe, paint)
        canvas.drawBitmap(pipeImage, null, upperPipeHead, paint)
    }

    override fun update() {
        pipeState = pipeState.copy(
            xPos = pipeState.xPos - pipeState.speed
        )
        if (pipeState.xPos <= (0 - pipeState.width - pipeState.extraPipeHeadWidth)) {
            callback.obstacleOffScreen(this)
        } else {
            val lowerObstacle = Rect(
                pipeState.xPos - pipeState.extraPipeHeadWidth,
                screenHeight - pipeState.height - pipeState.pipeHeadHeight,
                pipeState.xPos + pipeState.width + pipeState.extraPipeHeadWidth,
                screenHeight
            )
            val upperObstacle = Rect(
                pipeState.xPos - pipeState.extraPipeHeadWidth,
                0,
                pipeState.xPos + pipeState.width + pipeState.extraPipeHeadWidth,
                screenHeight - pipeState.height - pipeState.separation - pipeState.pipeHeadHeight
            )
            callback.obstaclePositionUpdate(this, listOf(lowerObstacle, upperObstacle))
        }
    }
}

data class PipeObstacleState(
    val height: Int = 0,
    val width: Int = 0,
    val pipeHeadHeight: Int = 0,
    val extraPipeHeadWidth: Int = 0,
    val separation: Int = 0,
    val xPos: Int = 0,
    val speed: Int = 0
)
