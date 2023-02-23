package com.example.flappybirdclone

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import com.example.flappybirdclone.sprites.Obstacle

class ObstacleManager constructor(
    private val resources: Resources,
    private val screenWidth: Int,
    private val screenHeight: Int,
    private val gameManagerCallback: GameManagerCallback
) : ObstacleCallBack {

    private val interval: Int = resources.getDimension(R.dimen.obstacle_interval).toInt()
    private val obstacleList: MutableList<Obstacle> = mutableListOf()

    private var progress: Int = 0
    private val speed: Int = resources.getDimension(R.dimen.obstacle_speed).toInt()

    init {
        obstacleList.add(Obstacle(resources, screenHeight, screenWidth, this))
    }

    fun draw(canvas: Canvas) {
        obstacleList.forEach {
            it.draw(canvas)
        }
    }

    fun update() {
        progress += speed
        if (progress > interval) {
            progress = 0
            obstacleList.add(
                Obstacle(
                    resources,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    callback = this
                )
            )
        }
        val duplicateList = mutableListOf<Obstacle>()
        duplicateList.addAll(obstacleList)
        duplicateList.forEach {
            it.update()
        }
    }

    override fun obstacleOffScreen(obstacle: Obstacle) {
        obstacleList.remove(obstacle)
        gameManagerCallback.removeObstacle(obstacle)
    }

    override fun obstaclePositionUpdate(obstacle: Obstacle, positions: List<Rect>) {
        gameManagerCallback.updateObstaclePos(obstacle = obstacle, positions = positions)
    }

    fun reset() {
        obstacleList.clear()
        obstacleList.add(Obstacle(resources, screenHeight, screenWidth, this))
        progress = 0
    }
}