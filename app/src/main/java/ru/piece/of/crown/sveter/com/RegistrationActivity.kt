package ru.piece.of.crown.sveter.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.light_orange, theme)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().add(R.id.container, RegistrationPhoneNumberFragment()).commit()
    }
}