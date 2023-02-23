package com.example.flappybirdclone

import android.graphics.Rect
import com.example.flappybirdclone.sprites.Obstacle

interface GameManagerCallback {
    fun updateBirdPos(birdPos: Rect)
    fun updateObstaclePos(obstacle: Obstacle, positions: List<Rect>)
    fun removeObstacle(obstacle: Obstacle)
}