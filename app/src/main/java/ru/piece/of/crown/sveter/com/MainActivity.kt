package ru.piece.of.crown.sveter.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var userAvatar: ImageView
    lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userAvatar = findViewById(R.id.userAvatar)
        userName = findViewById(R.id.userName)
    }
}
