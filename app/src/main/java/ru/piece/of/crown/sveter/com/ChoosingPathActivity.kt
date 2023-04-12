package ru.piece.of.crown.sveter.com

import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView


class ChoosingPathActivity : AppCompatActivity() {
    private lateinit var userRole: String

    private lateinit var mapView: MapView
    private lateinit var pointOfDepartureField: EditText
    private lateinit var pointOfArrivalField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!hasLocationPermissions())
            checkLocationPermissions()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing_path)

        userRole = intent.getStringExtra("role")!!

        mapView = findViewById(R.id.mapView)
        pointOfDepartureField = findViewById(R.id.pointOfDepartureField)
        pointOfArrivalField = findViewById(R.id.pointOfArrivalField)

        supportActionBar?.apply {
            if (Util.isDarkThemeNow(this@ChoosingPathActivity))
                setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_green, theme)))

            subtitle = resources.getString(R.string.choosePathSubtitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.white_cross)
            setHomeActionContentDescription(R.string.closeAction)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
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