package com.example.fireHunter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.labexam3.R

class GameScore : AppCompatActivity() {

    private lateinit var playAgainBtn: Button
    private lateinit var viewModel: GameScoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_score)

        viewModel = ViewModelProvider(this)[GameScoreViewModel::class.java]
        viewModel.init(this)

        val score = intent.getIntExtra("score", 0)
        var highScore = viewModel.getHighScore()

        if (score > highScore) {
            highScore = score
            viewModel.saveHighScore(highScore)
        }

        val endScoreTextView = findViewById<TextView>(R.id.endScore)
        endScoreTextView.text = "Score: $score\nHigh Score: $highScore"

        playAgainBtn = findViewById(R.id.playAgainBtn)
        playAgainBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
