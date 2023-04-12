package ru.piece.of.crown.sveter.com

import Util
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView


class ChoosingTravelPathActivity : AppCompatActivity() {
    private lateinit var userRole: String

    private lateinit var mapView: MapView
    private lateinit var pointOfDepartureField: EditText
    private lateinit var pointOfArrivalField: EditText
    private lateinit var applyPathPointsButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!hasLocationPermissions())
            checkLocationPermissions()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing_travel_path)

        userRole = intent.getStringExtra("role")!!

        mapView = findViewById(R.id.mapView)
        pointOfDepartureField = findViewById(R.id.pointOfDepartureField)
        pointOfArrivalField = findViewById(R.id.pointOfArrivalField)
        applyPathPointsButton = findViewById(R.id.applyPathPointsButton)

        applyPathPointsButton.setOnClickListener {
            startActivity(Intent(this, ChoosingOtherTravelParamentersActivity::class.java).apply {
                putExtra("role", userRole)
                putExtra("pointOfDeparture", pointOfDepartureField.text.toString())
                putExtra("pointOfArrival", pointOfArrivalField.text.toString())
            })
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }

        supportActionBar?.apply {
            if (Util.isDarkThemeNow(this@ChoosingTravelPathActivity))
                setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_green, theme)))

            subtitle = resources.getString(R.string.chooseTravelPathSubtitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.white_cross)
            setHomeActionContentDescription(R.string.closeAction)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return false
        }

        return false
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
        finish()
    }

    private fun hasLocationPermissions(): Boolean = ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun checkLocationPermissions() {
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {}
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {}
                else -> Toast.makeText(this, "Пожалуйста, предоставьте доступ к вашей геопозиции", Toast.LENGTH_LONG).show()
            }
        }.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION))
    }
}