package com.guyi.class25b_and_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.fragment.app.activityViewModels

class LeaderboardMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val viewModel: LeaderboardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_leaderboard_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val prefs = requireContext().getSharedPreferences("leaderboard", android.content.Context.MODE_PRIVATE)

        // Add markers for top 10 players
        for (i in 0 until 10) {
            val name = prefs.getString("player_$i", null)
            val lat = prefs.getString("lat_$i", null)?.toDoubleOrNull()
            val lng = prefs.getString("lng_$i", null)?.toDoubleOrNull()

            if (name != null && lat != null && lng != null) {
                val location = LatLng(lat, lng)
                googleMap.addMarker(MarkerOptions().position(location).title(name))
            }
        }

        // Move camera to first score
        val firstLat = prefs.getString("lat_0", null)?.toDoubleOrNull()
        val firstLng = prefs.getString("lng_0", null)?.toDoubleOrNull()
        if (firstLat != null && firstLng != null) {
            val firstLocation = LatLng(firstLat, firstLng)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
        }

        // Observe selected location from leaderboard list
        viewModel.selectedLocation.observe(viewLifecycleOwner) { latLng ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected"))
        }
    }
}
