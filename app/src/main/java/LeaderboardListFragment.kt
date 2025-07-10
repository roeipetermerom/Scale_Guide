package com.guyi.class25b_and_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import androidx.fragment.app.activityViewModels

class LeaderboardListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_leaderboard_list, container, false)
    }

    val viewModel: LeaderboardViewModel by activityViewModels()

    private fun getTopScores(): List<Triple<String, Int, LatLng>> {
        val prefs = requireContext().getSharedPreferences("leaderboard", Context.MODE_PRIVATE)
        return (0 until 10).map {
            val name = prefs.getString("player_$it", "Unknown") ?: "Unknown"
            val score = prefs.getInt("score_$it", 0)
            val lat = prefs.getString("lat_$it", null)?.toDoubleOrNull()
            val lng = prefs.getString("lng_$it", null)?.toDoubleOrNull()
            val loc = if (lat != null && lng != null) LatLng(lat, lng) else null
            Triple(name, score, loc ?: LatLng(0.0, 0.0))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val scores = getTopScores()
        recyclerView.adapter = ScoreAdapter(scores) { selectedLatLng ->
            viewModel.selectLocation(selectedLatLng)
        }
    }
}
