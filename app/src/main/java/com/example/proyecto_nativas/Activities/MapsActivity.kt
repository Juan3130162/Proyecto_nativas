package com.example.proyecto_nativas.Activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    private val ubicacionBogota = LatLng(4.6684065 , -74.1067738)
    private val ubicacionCali = LatLng(3.420556 , -76.522224)
    private val ubicacionGuajira = LatLng(10.7700 ,  -73.0039)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupButtonListeners()


}

        private fun setupButtonListeners() {
            findViewById<Button>(R.id.btn_bogota).setOnClickListener {
                moveToLocation(ubicacionBogota, "Bogotá")
            }

            findViewById<Button>(R.id.btn_cali).setOnClickListener {
                moveToLocation(ubicacionCali, "Cali")
            }

            findViewById<Button>(R.id.btn_guajira).setOnClickListener {
                moveToLocation(ubicacionGuajira, "Guajira")
            }


        }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionBogota, 15f))
        mMap.addMarker(MarkerOptions().position(ubicacionBogota).title("Bogotá"))
    }

    private fun moveToLocation(ubicacionBogota: LatLng, s: String) {

        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionBogota, 15f))
        mMap.addMarker(com.google.android.gms.maps.model.MarkerOptions().position(ubicacionBogota).title(s))


    }

}

