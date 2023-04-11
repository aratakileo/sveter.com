package ru.piece.of.crown.sveter.com

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SelectLoginOrRegistrationActivity : AppCompatActivity() {
    lateinit var iAmNewHereButton: Button
    lateinit var loginButton: Button
    lateinit var notifyButton: Button

    val CHANNEL_ID = "AnyThing0" //It does not matter what we put in
    val CHANNEL_NAME = "AnyThing1" //The same
    val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_login_or_registration)

        iAmNewHereButton = findViewById(R.id.iAmNewHereButton)
        loginButton = findViewById(R.id.loginButton)

        iAmNewHereButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        }

        //After Shurillovee's         https://www.youtube.com/watch?v=urn355_ymNA

        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Description")
            .setSmallIcon(R.drawable.notifications_bell)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager = NotificationManagerCompat.from(this)



        notifyButton.setOnClickListener {

            notificationManager.notify(NOTIFICATION_ID, notification) // I do not know why it is error
        }


        supportActionBar?.hide()
    }

    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}