package ru.piece.of.crown.sveter.com

import Util
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
            Util.startActivityWithHorizontalSlideAnimation(this, RegistrationActivity::class.java)
//            startActivity(Intent(this, RegistrationActivity::class.java))
//            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }

        loginButton.setOnClickListener {
            Util.startActivityWithHorizontalSlideAnimation(this, LoginActivity::class.java)
//            startActivity(Intent(this, LoginActivity::class.java))
//            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }

        supportActionBar?.hide()
    }
}