package com.example.flappybirdclone

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.flappybirdclone.sprites.*

class GameManager constructor(
    context: Context, attrSet: AttributeSet? = null
) : SurfaceView(context), SurfaceHolder.Callback, GameManagerCallback {

    private var thread: MainThread
    private lateinit var bird: Bird
    private lateinit var background: Background
    private lateinit var obstacleManager: ObstacleManager
    private lateinit var gameOver: GameOver
    private lateinit var gameMessage: GameMessage
    private lateinit var score: Score
    private val dm = DisplayMetrics()

    private var gameScore: Int = 0

    private var gameState by mutableStateOf(GameState.TO_START)
    private var lastEventTime: Long = 0L

    private lateinit var birdPosition: Rect
    private val obstacleMap: MutableMap<Obstacle, List<Rect>> = mutableMapOf()

    private val mpSwoosh: MediaPlayer = MediaPlayer.create(context, R.raw.swoosh)
    private val mpPoint: MediaPlayer = MediaPlayer.create(context, R.raw.point)
    private val mpDie: MediaPlayer = MediaPlayer.create(context, R.raw.die)
    private val mpHit: MediaPlayer = MediaPlayer.create(context, R.raw.hit)
    private val mpWing: MediaPlayer = MediaPlayer.create(context, R.raw.wing)

    init {
        holder.addCallback(this)
        thread = MainThread(holder, this)

        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        initGame()
    }

    private fun initGame() {
        bird = Bird(resources = resources, screenHeight = dm.heightPixels, this)
        background = Background(resources = resources, screenHeight = dm.heightPixels)
        obstacleManager = ObstacleManager(
            resources = resources,
            screenWidth = dm.widthPixels,
            screenHeight = dm.heightPixels,
            gameManagerCallback = this
        )
        gameOver = GameOver(
            resources = resources,
            screenWidth = dm.widthPixels,
            screenHeight = dm.heightPixels
        )

        gameMessage = GameMessage(
            resources = resources,
            screenWidth = dm.widthPixels,
            screenHeight = dm.heightPixels
        )

        gameScore = 0

        score = Score(
            resources = resources,
            screenHeight = dm.heightPixels,
            screenWidth = dm.widthPixels
        )
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        if (Thread.State.TERMINATED == thread.state) {
            thread = MainThread(holder, this)
        }
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        var isRetry = true
        while (isRetry) {
            try {
                thread.setRunning(false)
                thread.join()
                isRetry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawRGB(150, 255, 255)
        background.draw(canvas)
        bird.draw(canvas)
        when (gameState) {
            GameState.TO_START -> {
                gameMessage.draw(canvas)
                obstacleMap.clear()
            }
            GameState.IN_PROGRESS -> {
                obstacleManager.draw(canvas)
                score.draw(canvas)
                isCollision()
            }
            GameState.FINISHED -> {
                obstacleManager.draw(canvas)
                gameOver.draw(canvas)
                score.draw(canvas)
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val currentTime = System.currentTimeMillis()
        if (GameState.TO_START == gameState) {
            initGame()
            if (currentTime - lastEventTime >= 800) {
                gameState = GameState.IN_PROGRESS
                mpSwoosh.start()
            }
        } else if (GameState.FINISHED == gameState) {
            if (currentTime - lastEventTime >= 1000) {
                bird.reset()
                obstacleManager.reset()
                gameState = GameState.TO_START
                lastEventTime = System.currentTimeMillis()
            }
        } else {
            bird.onTouch()
            mpWing.start()
        }
        return true
    }

    fun update() {
        when (gameState) {
            GameState.TO_START -> {
                // stub
            }
            GameState.IN_PROGRESS -> {
                obstacleManager.update()
                bird.update()
            }
            GameState.FINISHED -> {
                bird.update()
            }
        }
    }

    override fun updateBirdPos(birdPos: Rect) {
        birdPosition = birdPos
    }

    override fun updateObstaclePos(obstacle: Obstacle, positions: List<Rect>) {
        if (obstacleMap.containsKey(obstacle)) {
            obstacleMap.remove(obstacle)
        }
        obstacleMap[obstacle] = positions
    }

    override fun removeObstacle(obstacle: Obstacle) {
        obstacleMap.remove(obstacle)
        gameScore++
        score.updateScore(gameScore)
        mpPoint.start()
    }

    private fun isCollision() {
        var collision = false
        if (birdPosition.bottom > dm.heightPixels) {
            collision = true
        } else {
            obstacleMap.keys.forEach {
                val bottomRect = obstacleMap[it]!![0]
                val topRect = obstacleMap[it]!![1]

                if (birdPosition.intersect(bottomRect) || birdPosition.intersect(topRect)) {
                    collision = true
                }
            }
        }

        if (collision) {
            bird.collision()
            score.collision(
                context.getSharedPreferences(
                    resources.getString(R.string.app_name),
                    Context.MODE_PRIVATE
                )
            )
            gameState = GameState.FINISHED
            mpHit.apply {
                this.start()
                this.setOnCompletionListener {
                    mpDie.start()
                }
            }
            lastEventTime = System.currentTimeMillis()
        }
    }
}

enum class GameState {
    TO_START,
    IN_PROGRESS,
    FINISHED
}

