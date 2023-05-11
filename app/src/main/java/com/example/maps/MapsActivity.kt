package com.example.maps

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.maps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import org.w3c.dom.Text
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val REQUEST_LOCATION_PERMISSION = 1
    private final val LATITUDE = 42.99377771656961
    private final val LONGITUDE = -81.2170278330648
    private final val SCHOOL_LATITUDE = 42.988264885587526
    private final val SCHOOL_LONGITUDE = -81.23460803468575
    private final val ADDRESS = "428 Charlotte Street"
    private final val SCHOOL_NAME = "H.B. Beal Secondary School"


    private var lat: Double = LATITUDE
    private var lon: Double = LONGITUDE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.title = "Project 2, Jeril Johnson Nedumbakaran"
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        val zoomLevel = 15f
        //1 - world
        //5 - landmass/continent
        //10 - city
        //15 - street
        //20 - buildings

        // Add a marker in Sydney and move the camera
        val myLocation = LatLng(LATITUDE, LONGITUDE)
        val schoolLocation = LatLng(SCHOOL_LATITUDE, SCHOOL_LONGITUDE)
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.5f, Long: %2$.5f",
            LATITUDE,
            LONGITUDE
        )
        map.addMarker(MarkerOptions().position(myLocation).title(ADDRESS).snippet(snippet))
        map.addMarker(MarkerOptions().position(schoolLocation).title(SCHOOL_NAME))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,zoomLevel))
        setMapLongClick(map)
        val overlaySize = 300f
        val androidOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory
                .fromResource(R.drawable.school))
            .position(schoolLocation, overlaySize)
        map.addGroundOverlay(androidOverlay)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setMapLongClick(map:GoogleMap) {
        map.setOnMapClickListener { latlng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latlng.latitude,
                latlng.longitude
            )
            map.addMarker(
                MarkerOptions().position(latlng).title(getString(R.string.dropped_pin)).snippet(snippet)
            )
            lat = latlng.latitude
            lon = latlng.longitude
        }
    }

    private fun setPoiClick(map:GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMaker = map.addMarker(
                MarkerOptions().position(poi.latLng).title(poi.name)
            )
            poiMaker?.showInfoWindow()
            lat = poi.latLng.latitude
            lon = poi.latLng.longitude
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onButtonClick(view: View) {
        var lat_value = findViewById<TextView>(R.id.lat_value)
        var lon_value = findViewById<TextView>(R.id.long_value)
        when(view.id) {
            R.id.displayButton->{
                lon_value.text = lon.toString()
                lat_value.text = lat.toString()
            }
        }
    }
}