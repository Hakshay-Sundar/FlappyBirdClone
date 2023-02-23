package com.example.flappybirdclone

import android.graphics.Canvas
import android.view.SurfaceHolder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainThread constructor(
    private val surfaceHolder: SurfaceHolder,
    private val gameManager: GameManager
) : Thread() {

    var data by mutableStateOf(MainThreadData(false))
        private set

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        while (data.isRunning) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameManager.update()
                    gameManager.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = (1000.toLong() / data.targetFPS) - timeMillis

            try {
                if (waitTime > 0) {
                    sleep(waitTime)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setRunning(isRunning: Boolean) {
        data = data.copy(
            isRunning = isRunning
        )
    }

    companion object {
        private var canvas: Canvas? = null
    }
}

data class MainThreadData(
    val isRunning: Boolean = false,
    val targetFPS: Int = 60,
)