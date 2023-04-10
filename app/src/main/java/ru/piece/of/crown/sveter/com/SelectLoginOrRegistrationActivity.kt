package ru.piece.of.crown.sveter.com

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectLoginOrRegistrationActivity : AppCompatActivity() {
    lateinit var iAmNewHereButton: Button
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_login_or_registration)

        iAmNewHereButton = findViewById(R.id.iAmNewHereButton)
        loginButton = findViewById(R.id.loginButton)

        iAmNewHereButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        supportActionBar?.hide()
    }
}