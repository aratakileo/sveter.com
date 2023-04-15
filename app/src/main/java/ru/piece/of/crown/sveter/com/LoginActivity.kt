package ru.piece.of.crown.sveter.com

import android.os.Bundle

class LoginActivity : RegistrationActivity(false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}