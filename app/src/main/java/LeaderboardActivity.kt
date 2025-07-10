package com.guyi.class25b_and_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels

class LeaderboardActivity : AppCompatActivity() {

    val viewModel: LeaderboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}