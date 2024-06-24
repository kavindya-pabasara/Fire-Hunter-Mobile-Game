package com.example.fireHunter

import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.SharedPreferences

class GameScoreViewModel : ViewModel() {
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        }
    }

    fun getHighScore(): Int {
        return sharedPreferences?.getInt("highScore", 0) ?: 0
    }

    fun saveHighScore(score: Int) {
        val editor = sharedPreferences?.edit()
        editor?.putInt("highScore", score)
        editor?.apply()
    }
}
