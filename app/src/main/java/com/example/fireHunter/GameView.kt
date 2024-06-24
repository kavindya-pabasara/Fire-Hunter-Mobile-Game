package com.example.fireHunter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import com.example.labexam3.R

class GameView(context: Context, private val gameTask: GameTask) : View(context) {
    private var speed = 30
    private var score = 0
    private var highScore = 0
    private var dragonPosition = width / 4f

    private val dragonDrawable: Drawable = context.getDrawable(R.drawable.dragon)!!
    private val fireballDrawable: Drawable = context.getDrawable(R.drawable.fireball)!!
    private val dragonWidth = dragonDrawable.intrinsicWidth
    private val dragonHeight = dragonDrawable.intrinsicHeight
    private val fireballWidth = fireballDrawable.intrinsicWidth / 4
    private val fireballHeight = fireballDrawable.intrinsicHeight / 4

    private val fireballs = mutableListOf<Fireball>()
    private var mediaPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.game_music)

    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        textAlign = Paint.Align.LEFT
    }

    init {
        dragonDrawable.setBounds(0, 0, dragonWidth, dragonHeight)
        fireballDrawable.setBounds(0, 0, fireballWidth, fireballHeight)
        setBackgroundResource(R.drawable.sky)
        mediaPlayer?.isLooping = true

        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("highScore", 0)
    }

    fun resetGame() {
        score = 0
        fireballs.clear()
        speed = 30
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer.create(context, R.raw.game_music)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dragonX = dragonPosition - dragonWidth / 6
        dragonDrawable.setBounds(dragonX.toInt(), height - dragonHeight, (dragonX + dragonWidth).toInt(), height)
        dragonDrawable.draw(canvas)

        val iterator = fireballs.iterator()
        while (iterator.hasNext()) {
            val fireball = iterator.next()
            fireball.y += speed

            if (fireball.y > height) {
                iterator.remove()
                mediaPlayer?.pause()
                if (score > highScore) {
                    highScore = score
                }
                gameTask.gameOver(score, highScore)
                return
            }

            val fireballX = fireball.x
            fireballDrawable.setBounds(fireballX, fireball.y, fireballX + fireballWidth, fireball.y + fireballHeight)
            fireballDrawable.draw(canvas)

            if (fireballX < dragonX + dragonWidth && fireballX + fireballWidth > dragonX &&
                fireball.y + fireballHeight > height - dragonHeight) {
                score++
                if (score % 5 == 0) {
                    speed += 5
                }
                iterator.remove()
                gameTask.updateScore(score)
            }
        }

        canvas.drawText("Score: $score", 20f, 70f, scorePaint)
        canvas.drawText("High Score: $highScore", 20f, 140f, scorePaint)

        if (fireballs.isEmpty()) {
            fireballs.add(Fireball((Math.random() * (width - fireballWidth)).toInt(), -fireballHeight))
        }

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            dragonPosition = event.x
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    data class Fireball(var x: Int, var y: Int)
}
