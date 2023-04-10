package ru.piece.of.crown.sveter.com

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
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

        val rippleColorStateList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(R.color.light_green))

        iAmNewHereButton.background = RippleDrawable(rippleColorStateList, iAmNewHereButton.background, null)
        loginButton.background = RippleDrawable(rippleColorStateList, loginButton.background, null)

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