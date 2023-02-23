package com.example.flappybirdclone.sprites

import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.flappybirdclone.R

class Score constructor(
    private val resources: Resources,
    private val screenHeight: Int,
    private val screenWidth: Int,
) : Sprite {

    private val zero: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.zero)
    private val one: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.one)
    private val two: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.two)
    private val three: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.three)
    private val four: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.four)
    private val five: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.five)
    private val six: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.six)
    private val seven: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.seven)
    private val eight: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.eight)
    private val nine: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.nine)

    private val bestBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.best)
    private val scoreBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.score)

    private val scoreBitMapMap: MutableMap<Int, Bitmap> = mutableMapOf()

    private var currentScore: Int = 0
    private var bestScore: Int = 0 //todo - Fetch this value from the local db.
    private var hasCollided: Boolean = false

    init {
        initialiseMap()
    }

    private fun initialiseMap() {
        scoreBitMapMap[0] = zero
        scoreBitMapMap[1] = one
        scoreBitMapMap[2] = two
        scoreBitMapMap[3] = three
        scoreBitMapMap[4] = four
        scoreBitMapMap[5] = five
        scoreBitMapMap[6] = six
        scoreBitMapMap[7] = seven
        scoreBitMapMap[8] = eight
        scoreBitMapMap[9] = nine
    }

    override fun draw(canvas: Canvas) {
        if (!hasCollided) {
            val tempScoreObject = currentScore
            val bitmapScore = fetchScoreInBitmap(tempScoreObject)
            bitmapScore.forEachIndexed { index, bitmap ->
                val xPos =
                    (screenWidth / 2) - (bitmapScore.size * zero.width / 2) + (zero.width * index)
                val yPos = (screenHeight / 8) - (zero.height / 2)
                canvas.drawBitmap(bitmap, xPos.toFloat(), yPos.toFloat(), null)
            }
        } else {
            val score = fetchScoreInBitmap(score = currentScore)
            val bestScore = fetchScoreInBitmap(score = bestScore)

            canvas.drawBitmap(
                scoreBitmap,
                ((screenWidth / 4) - scoreBitmap.width / 2).toFloat(),
                ((screenHeight / 8) - zero.height - scoreBitmap.height).toFloat(),
                null
            )
            score.forEachIndexed { index, bitmap ->
                val xPos =
                    (screenWidth / 4) - (score.size * zero.width / 2) + (zero.width * index)
                val yPos = (screenHeight / 8) - (zero.height / 2)
                canvas.drawBitmap(bitmap, xPos.toFloat(), yPos.toFloat(), null)
            }


            canvas.drawBitmap(
                bestBitmap,
                ((3 * screenWidth / 4) - bestBitmap.width / 2).toFloat(),
                ((screenHeight / 8) - zero.height - bestBitmap.height).toFloat(),
                null
            )
            bestScore.forEachIndexed { index, bitmap ->
                val xPos =
                    (3 * screenWidth / 4) - (bestScore.size * zero.width / 2) + (zero.width * index)
                val yPos = (screenHeight / 8) - (zero.height / 2)
                canvas.drawBitmap(bitmap, xPos.toFloat(), yPos.toFloat(), null)
            }
        }
    }

    private fun fetchScoreInBitmap(score: Int): List<Bitmap> {
        val list: MutableList<Bitmap> = mutableListOf()
        return if (score == 0) {
            list.add(zero)
            list
        } else {
            var tempScore = score
            while (tempScore > 0) {
                list.add(scoreBitMapMap[tempScore % 10]!!)
                tempScore /= 10
            }
            list.reversed()
        }
    }

    override fun update() {
        // stub
    }

    fun updateScore(updatedScore: Int) {
        currentScore = updatedScore
    }

    fun collision(prefs: SharedPreferences) {
        hasCollided = true
        bestScore = prefs.getInt(BEST_SCORE, 0)

        if (currentScore > bestScore) {
            bestScore = currentScore
            prefs.edit().putInt(BEST_SCORE, bestScore).apply()
        }
    }

    companion object {
        const val BEST_SCORE = "best_score"
    }
}