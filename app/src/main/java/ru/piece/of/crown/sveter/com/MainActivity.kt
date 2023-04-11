package ru.piece.of.crown.sveter.com

import UserData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    lateinit var userAvatar: ImageView
    lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!UserData.isUserAlreadyLogin(this)) {
            startActivity(Intent(this, SelectLoginOrRegistrationActivity::class.java))
            finish()
        } else
            println("Login with phone number: ${UserData.getPhoneNumber(this)}")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        userAvatar = findViewById(R.id.userAvatar)
        userName = findViewById(R.id.userName)

        UserData.getUserFirstName(this).apply {
            println(this)
            if (this != null) userName.text = this
        }
    }
}
