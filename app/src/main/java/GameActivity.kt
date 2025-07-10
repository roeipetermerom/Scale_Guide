package com.guyi.class25b_and_1

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guyi.class25b_and_1.databinding.ActivityGameBinding
import java.util.*
import android.os.Handler
import android.os.Looper
import android.content.Intent
import com.google.android.gms.maps.model.LatLng
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var hearts: Array<AppCompatImageView>
    private lateinit var obstaclesLane1: Array<ImageView>
    private lateinit var obstaclesLane1b: Array<ImageView>
    private lateinit var obstaclesLane2: Array<ImageView>
    private lateinit var obstaclesLane2b: Array<ImageView>
    private lateinit var obstaclesLane3: Array<ImageView>
    private lateinit var obstaclesLane3b: Array<ImageView>
    private lateinit var obstaclesLane4: Array<ImageView>
    private lateinit var obstaclesLane4b: Array<ImageView>
    private lateinit var obstaclesLane5: Array<ImageView>
    private lateinit var obstaclesLane5b: Array<ImageView>
    private lateinit var hamsaLane1: Array<ImageView>
    private lateinit var hamsaLane2: Array<ImageView>
    private lateinit var hamsaLane3: Array<ImageView>
    private lateinit var hamsaLane4: Array<ImageView>
    private lateinit var hamsaLane5: Array<ImageView>
    private lateinit var clubs: Array<ImageView>
    private lateinit var stars: Array<ImageView>
    private lateinit var carLanes: Array<ImageView>


    private var currentIndex1 = 0
    private var currentIndex1b = 8
    private var currentIndex2 = 8
    private var currentIndex2b = 8
    private var currentIndex3 = 8
    private var currentIndex3b = 8
    private var currentIndex4 = 8
    private var currentIndex4b = 8
    private var currentIndex5 = 8
    private var currentIndex5b = 8

    private var currentIndexHamsa1 = 8
    private var currentIndexHamsa2 = 8
    private var currentIndexHamsa3 = 8
    private var currentIndexHamsa4 = 8
    private var currentIndexHamsa5 = 8

    private var yn = 1
    private val random = Random()
    private var currentCarIndex = 1
    private var lives = 3
    private var mediaPlayer: MediaPlayer? = null

    private var gameMode: String? = null
    private var speedMillis: Long = 1000

    private var meters = 0
    private var gameTimer: Timer? = null

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var sensorListener: SensorEventListener? = null

    private val FAST_SPEED = 500L
    private val SLOW_SPEED = 1000L


    private fun makeObstaclesInvisible() {
        (obstaclesLane1 + obstaclesLane2 + obstaclesLane3 + obstaclesLane4 + obstaclesLane5 + hamsaLane1 + hamsaLane2 + hamsaLane3 + hamsaLane4 + hamsaLane5 + clubs + stars).forEach {
            it.visibility = View.INVISIBLE
        }
    }

    private fun generateObstacle() {
        var firstNum = 8
        repeat(2) {
            val laneIndex = random.nextInt(5)  // picks which lane/stream
            val showHamsa = random.nextInt(12) == 0  // 1-in-12 chance

            if(laneIndex != firstNum){
                when (laneIndex) {
                    0 -> if (showHamsa && currentIndexHamsa1 == 8) currentIndexHamsa1 = 0
                    else if (currentIndex1 == 8) currentIndex1 = 0
                    else if (currentIndex1b == 8) currentIndex1b = 0

                    1 -> if (showHamsa && currentIndexHamsa2 == 8) currentIndexHamsa2 = 0
                    else if (currentIndex2 == 8) currentIndex2 = 0
                    else if (currentIndex2b == 8) currentIndex2b = 0

                    2 -> if (showHamsa && currentIndexHamsa3 == 8) currentIndexHamsa3 = 0
                    else if (currentIndex3 == 8) currentIndex3 = 0
                    else if (currentIndex3b == 8) currentIndex3b = 0

                    3 -> if (showHamsa && currentIndexHamsa4 == 8) currentIndexHamsa4 = 0
                    else if (currentIndex4 == 8) currentIndex4 = 0
                    else if (currentIndex4b == 8) currentIndex4b = 0

                    4 -> if (showHamsa && currentIndexHamsa5 == 8) currentIndexHamsa5 = 0
                    else if (currentIndex5 == 8) currentIndex5 = 0
                    else if (currentIndex5b == 8) currentIndex5b = 0

                }
                firstNum = laneIndex
            }
        }
    }


    private fun updateObstaclePosition() {
        makeObstaclesInvisible()

        fun update(index: Int, array: Array<ImageView>): Int {
            if (index < array.size + 1) {
                if (index < 7) array[index].visibility = View.VISIBLE
                return index + 1
            }
            return index
        }

        currentIndex1 = update(currentIndex1, obstaclesLane1)
        currentIndex2 = update(currentIndex2, obstaclesLane2)
        currentIndex3 = update(currentIndex3, obstaclesLane3)
        currentIndex4 = update(currentIndex4, obstaclesLane4)
        currentIndex5 = update(currentIndex5, obstaclesLane5)
        currentIndex1b = update(currentIndex1b, obstaclesLane1b)
        currentIndex2b = update(currentIndex2b, obstaclesLane2b)
        currentIndex3b = update(currentIndex3b, obstaclesLane3b)
        currentIndex4b = update(currentIndex4b, obstaclesLane4b)
        currentIndex5b = update(currentIndex5b, obstaclesLane5b)

        currentIndexHamsa1 = update(currentIndexHamsa1, hamsaLane1)
        currentIndexHamsa2 = update(currentIndexHamsa2, hamsaLane2)
        currentIndexHamsa3 = update(currentIndexHamsa3, hamsaLane3)
        currentIndexHamsa4 = update(currentIndexHamsa4, hamsaLane4)
        currentIndexHamsa5 = update(currentIndexHamsa5, hamsaLane5)
    }

    private fun moveCarLeft() {
        if (currentCarIndex > 0) {
            carLanes[currentCarIndex].visibility = View.INVISIBLE
            currentCarIndex--
            carLanes[currentCarIndex].visibility = View.VISIBLE
        }
    }

    private fun moveCarRight() {
        if (currentCarIndex < carLanes.size - 1) {
            carLanes[currentCarIndex].visibility = View.INVISIBLE
            currentCarIndex++
            carLanes[currentCarIndex].visibility = View.VISIBLE
        }
    }

    private fun enableSensorControls() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0] // left/right
                val y = event.values[1] // forward/backward tilt

                // Move car left/right
                if (x > 4 && currentCarIndex > 0) {
                    moveCarLeft()
                } else if (x < -4 && currentCarIndex < carLanes.size - 1) {
                    moveCarRight()
                }

                // Tilt forward (y < 0) = speed up
                // Tilt backward (y > 0) = slow down
                val newSpeed = if (y < -4) FAST_SPEED else SLOW_SPEED

                if (newSpeed != speedMillis) {
                    speedMillis = newSpeed
                    restartGameTimer()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        // Hide button controls
        findViewById<View>(R.id.btn_left).visibility = View.GONE
        findViewById<View>(R.id.btn_right).visibility = View.GONE
    }

    private fun restartGameTimer() {
        gameTimer?.cancel()
        gameTimer = Timer()
        gameTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    updateObstaclePosition()
                    checkCollision()
                    if (yn % 2 == 0) generateObstacle()
                    yn++
                    meters++
                    binding.tvDistance.text = "Meters: $meters"
                }
            }
        }, 0, speedMillis)
    }


    private fun showCollisionEffects() {
        Toast.makeText(this, "CRASH!", Toast.LENGTH_SHORT).show()

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(300)
        }

        val crashSound = MediaPlayer.create(this, R.raw.collision_sound)
        crashSound.start()

        when (currentCarIndex) {
            0 -> clubs[4].visibility = View.VISIBLE
            1 -> clubs[3].visibility = View.VISIBLE
            2 -> clubs[2].visibility = View.VISIBLE
            3 -> clubs[1].visibility = View.VISIBLE
            4 -> clubs[0].visibility = View.VISIBLE
        }

        lives--
        if (lives <= 0) {
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show()

            // Cancel the timer
            gameTimer?.cancel()
            gameTimer = null

            val laneIndex = random.nextInt(5)  // picks which lane/stream
            val showHamsa = random.nextInt(12) == 0  // 1-in-12 chance


            when (random.nextInt(4)) {
                0 -> saveScoreToLeaderboard(meters, "Roei", 32.0853, 34.7818)

                1 -> saveScoreToLeaderboard(meters, "Avi", 32.021526, 34.739479)

                2 -> saveScoreToLeaderboard(meters, "Harry", 54.160272, -1.655470)

                3 -> saveScoreToLeaderboard(meters, "Trump", 38.8977, 77.0365)

            }


            // Delay briefly before returning to menu (optional)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LeaderboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }, 1000) // 1 second delay
        }
        updateHeartsUI()
    }

    private fun coinCollision() {
        Toast.makeText(this, "Hamsa!", Toast.LENGTH_SHORT).show()

        val coinSound = MediaPlayer.create(this, R.raw.hamsa_sound)
        coinSound.start()

        when (currentCarIndex) {
            0 -> stars[4].visibility = View.VISIBLE
            1 -> stars[3].visibility = View.VISIBLE
            2 -> stars[2].visibility = View.VISIBLE
            3 -> stars[1].visibility = View.VISIBLE
            4 -> stars[0].visibility = View.VISIBLE
        }

        if(lives < 3){
            lives++
            updateHeartsUI()
        }

    }

    private fun updateHeartsUI() {
        for (i in hearts.indices) {
            hearts[i].visibility = if (i < lives) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun checkCollision() {
        if (((currentIndex1 == 7 || currentIndex1b == 7) && currentCarIndex == 0)
            || ((currentIndex2 == 7 || currentIndex2b == 7) && currentCarIndex == 1)
            || ((currentIndex3 == 7 || currentIndex3b == 7) && currentCarIndex == 2)
            || ((currentIndex4 == 7 || currentIndex4b == 7) && currentCarIndex == 3)
            || ((currentIndex5 == 7 || currentIndex5b == 7) && currentCarIndex == 4)
        ) {
            showCollisionEffects()
        }
        if ((currentIndexHamsa1 == 7 && currentCarIndex == 0)
            || (currentIndexHamsa2 == 7 && currentCarIndex == 1)
            || (currentIndexHamsa3 == 7 && currentCarIndex == 2)
            || (currentIndexHamsa4 == 7 && currentCarIndex == 3)
            || (currentIndexHamsa5 == 7 && currentCarIndex == 4)
        ) {
            coinCollision()
        }
    }

    private fun saveScoreToLeaderboard(score: Int, playerName: String = "You", lat: Double, lng: Double) {
        val prefs = getSharedPreferences("leaderboard", Context.MODE_PRIVATE)

        val scores = (0 until 10).map {
            val name = prefs.getString("player_$it", "Unknown") ?: "Unknown"
            val savedScore = prefs.getInt("score_$it", 0)
            val latStr = prefs.getString("lat_$it", null)
            val lngStr = prefs.getString("lng_$it", null)
            val coords = if (latStr != null && lngStr != null) LatLng(latStr.toDouble(), lngStr.toDouble()) else null
            Triple(name, savedScore, coords)
        }.toMutableList()

        // Add and sort
        scores.add(Triple(playerName, score, LatLng(lat, lng)))
        val sorted = scores.sortedByDescending { it.second }.take(10)

        with(prefs.edit()) {
            sorted.forEachIndexed { index, (name, score, location) ->
                putString("player_$index", name)
                putInt("score_$index", score)
                if (location != null) {
                    putString("lat_$index", location.latitude.toString())
                    putString("lng_$index", location.longitude.toString())
                }
            }
            apply()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        //EdgeToEdge.enable(this)
        setContentView(binding.root)

        gameMode = intent.getStringExtra("mode")

        if (gameMode == "sensor") {
            enableSensorControls()
        }

        speedMillis = when (gameMode) {
            "fast" -> FAST_SPEED
            else -> SLOW_SPEED
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.game_music)
        mediaPlayer?.start()

        obstaclesLane1 = arrayOf(
            findViewById(R.id.obstacle_1),
            findViewById(R.id.obstacle_6),
            findViewById(R.id.obstacle_11),
            findViewById(R.id.obstacle_16),
            findViewById(R.id.obstacle_21),
            findViewById(R.id.obstacle_26),
            findViewById(R.id.obstacle_31)
        )
        obstaclesLane1b = obstaclesLane1

        obstaclesLane2 = arrayOf(
            findViewById(R.id.obstacle_2),
            findViewById(R.id.obstacle_7),
            findViewById(R.id.obstacle_12),
            findViewById(R.id.obstacle_17),
            findViewById(R.id.obstacle_22),
            findViewById(R.id.obstacle_27),
            findViewById(R.id.obstacle_32)
        )
        obstaclesLane2b = obstaclesLane2

        obstaclesLane3 = arrayOf(
            findViewById(R.id.obstacle_3),
            findViewById(R.id.obstacle_8),
            findViewById(R.id.obstacle_13),
            findViewById(R.id.obstacle_18),
            findViewById(R.id.obstacle_23),
            findViewById(R.id.obstacle_28),
            findViewById(R.id.obstacle_33)
        )
        obstaclesLane3b = obstaclesLane3

        obstaclesLane4 = arrayOf(
            findViewById(R.id.obstacle_4),
            findViewById(R.id.obstacle_9),
            findViewById(R.id.obstacle_14),
            findViewById(R.id.obstacle_19),
            findViewById(R.id.obstacle_24),
            findViewById(R.id.obstacle_29),
            findViewById(R.id.obstacle_34)
        )
        obstaclesLane4b = obstaclesLane4

        obstaclesLane5 = arrayOf(
            findViewById(R.id.obstacle_5),
            findViewById(R.id.obstacle_10),
            findViewById(R.id.obstacle_15),
            findViewById(R.id.obstacle_20),
            findViewById(R.id.obstacle_25),
            findViewById(R.id.obstacle_30),
            findViewById(R.id.obstacle_35)
        )
        obstaclesLane5b = obstaclesLane5

        hamsaLane1 = arrayOf(
            findViewById(R.id.hamsa_1),
            findViewById(R.id.hamsa_6),
            findViewById(R.id.hamsa_11),
            findViewById(R.id.hamsa_16),
            findViewById(R.id.hamsa_21),
            findViewById(R.id.hamsa_26),
            findViewById(R.id.hamsa_31)
        )

        hamsaLane2 = arrayOf(
            findViewById(R.id.hamsa_2),
            findViewById(R.id.hamsa_7),
            findViewById(R.id.hamsa_12),
            findViewById(R.id.hamsa_17),
            findViewById(R.id.hamsa_22),
            findViewById(R.id.hamsa_27),
            findViewById(R.id.hamsa_32)
        )

        hamsaLane3 = arrayOf(
            findViewById(R.id.hamsa_3),
            findViewById(R.id.hamsa_8),
            findViewById(R.id.hamsa_13),
            findViewById(R.id.hamsa_18),
            findViewById(R.id.hamsa_23),
            findViewById(R.id.hamsa_28),
            findViewById(R.id.hamsa_33)
        )

        hamsaLane4 = arrayOf(
            findViewById(R.id.hamsa_4),
            findViewById(R.id.hamsa_9),
            findViewById(R.id.hamsa_14),
            findViewById(R.id.hamsa_19),
            findViewById(R.id.hamsa_24),
            findViewById(R.id.hamsa_29),
            findViewById(R.id.hamsa_34)
        )

        hamsaLane5 = arrayOf(
            findViewById(R.id.hamsa_5),
            findViewById(R.id.hamsa_10),
            findViewById(R.id.hamsa_15),
            findViewById(R.id.hamsa_20),
            findViewById(R.id.hamsa_25),
            findViewById(R.id.hamsa_30),
            findViewById(R.id.hamsa_35)
        )


        clubs = arrayOf(
            findViewById(R.id.club_1),
            findViewById(R.id.club_2),
            findViewById(R.id.club_3),
            findViewById(R.id.club_4),
            findViewById(R.id.club_5),
        )

        stars = arrayOf(
            findViewById(R.id.star_1),
            findViewById(R.id.star_2),
            findViewById(R.id.star_3),
            findViewById(R.id.star_4),
            findViewById(R.id.star_5),
        )

        makeObstaclesInvisible()

        carLanes = arrayOf(
            findViewById(R.id.avi_1),
            findViewById(R.id.avi_2),
            findViewById(R.id.avi_3),
            findViewById(R.id.avi_4),
            findViewById(R.id.avi_5),
        )
        carLanes.forEach { it.visibility = View.INVISIBLE }
        carLanes[currentCarIndex].visibility = View.VISIBLE

        findViewById<View>(R.id.btn_left).setOnClickListener { moveCarLeft() }
        findViewById<View>(R.id.btn_right).setOnClickListener { moveCarRight() }

        gameTimer = Timer()
        gameTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    updateObstaclePosition()
                    checkCollision()
                    if (yn % 2 == 0) generateObstacle()
                    yn++
                    meters++
                    binding.tvDistance.text = "Meters: $meters"
                }
            }
        }, 0, speedMillis)

        hearts = arrayOf(
            binding.imgHeart1,
            binding.imgHeart2,
            binding.imgHeart3
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        sensorListener?.let {
            sensorManager.unregisterListener(it)
        }
    }
}
