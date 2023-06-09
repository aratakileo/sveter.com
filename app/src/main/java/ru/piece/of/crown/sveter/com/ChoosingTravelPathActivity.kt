package ru.piece.of.crown.sveter.com

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
import androidx.core.widget.doAfterTextChanged
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import isDarkThemeNow


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

        applyPathPointsButton.apply {
            alpha = 0f
            translationX = 50f
            isEnabled = false
        }

        applyPathPointsButton.setOnClickListener {
            startActivity(Intent(this, ChoosingOtherTravelParametersActivity::class.java).apply {
                putExtra("role", userRole)
                putExtra("pointOfDeparture", pointOfDepartureField.text.toString().trim())
                putExtra("pointOfArrival", pointOfArrivalField.text.toString().trim())
            })
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }

        supportActionBar?.apply {
            if (isDarkThemeNow)
                setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_green, theme)))

            subtitle = resources.getString(R.string.chooseTravelPathSubtitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.white_cross)
            setHomeActionContentDescription(R.string.closeAction)
        }

        pointOfDepartureField.doAfterTextChanged {
            tryToShowOrHideApplyPathPointsButton()
        }

        pointOfArrivalField.doAfterTextChanged {
            tryToShowOrHideApplyPathPointsButton()
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
    
    private fun tryToShowOrHideApplyPathPointsButton() {
        if (pointOfDepartureField.length() >= 3 && pointOfArrivalField.length() >= 3) {
            if (!applyPathPointsButton.isEnabled)
                applyPathPointsButton.apply {
                    isEnabled = true
                    animate().alpha(1f).translationX(0f).duration = 1000
                }
        } else if (applyPathPointsButton.isEnabled)
            applyPathPointsButton.apply {
                isEnabled = false
                animate().alpha(0f).translationX(50f).duration = 1000
            }
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