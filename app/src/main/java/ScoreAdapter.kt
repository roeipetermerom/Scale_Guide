package com.guyi.class25b_and_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng

class ScoreAdapter(
    private val scores: List<Triple<String, Int, LatLng>>,
    private val clickCallback: (LatLng) -> Unit
) : RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.tv_player_name)
        val playerScore: TextView = view.findViewById(R.id.tv_player_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val (name, score, location) = scores[position]
        holder.playerName.text = name
        holder.playerScore.text = "$score meters"

        holder.itemView.setOnClickListener {
            clickCallback(location) // Notify fragment to update the map
        }
    }

    override fun getItemCount(): Int = scores.size
}
