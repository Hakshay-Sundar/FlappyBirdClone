package com.example.flappybirdclone

import android.graphics.Rect
import com.example.flappybirdclone.sprites.Obstacle

interface ObstacleCallBack {
    fun obstacleOffScreen(obstacle: Obstacle)
    fun obstaclePositionUpdate(obstacle: Obstacle, positions: List<Rect>)
}