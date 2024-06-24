package com.example.fireHunter

interface GameTask {
    fun updateScore(score: Int)
    fun gameOver(score: Int, highScore: Int)
}
