package com.example.fireHunter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.labexam3.R

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var gameView: GameView
    private lateinit var scoreTextView: TextView
    private lateinit var startBtn: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.score)
        startBtn = findViewById(R.id.startBtn)
        gameView = GameView(this, this)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        startBtn.setOnClickListener {
            gameView.resetGame()
            addContentView(gameView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ))
            startBtn.visibility = View.GONE
            scoreTextView.visibility = View.GONE
        }

        val highScore = sharedPreferences.getInt("highScore", 0)
        scoreTextView.text = "High Score: $highScore"
    }

    override fun updateScore(score: Int) {
        runOnUiThread {
            scoreTextView.text = "Score: $score"
        }
    }

    override fun gameOver(score: Int, highScore: Int) {
        runOnUiThread {
            if (score > highScore) {
                saveHighScore(score)
            }

            val intent = Intent(this@MainActivity, GameScore::class.java)
            intent.putExtra("score", score)
            intent.putExtra("highScore", getHighScore())
            startActivity(intent)
        }
    }

    private fun saveHighScore(highScore: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("highScore", highScore)
        editor.apply()
    }

    private fun getHighScore(): Int {
        return sharedPreferences.getInt("highScore", 0)
    }
}
