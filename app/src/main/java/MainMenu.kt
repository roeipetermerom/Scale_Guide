package com.guyi.class25b_and_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guyi.class25b_and_1.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnButtonSlow.setOnClickListener {
            startGame("slow")
        }

        binding.btnButtonFast.setOnClickListener {
            startGame("fast")
        }

        binding.btnSensorMode.setOnClickListener {
            startGame("sensor")
        }

        binding.btnLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGame(mode: String) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("mode", mode)
        startActivity(intent)
    }
}
