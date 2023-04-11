package ru.piece.of.crown.sveter.com

import VerificationCodeSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var startGettingCode = false

        val verificationCodeSender = VerificationCodeSender(
            resources.getString(R.string.smsRuAPIToken),
            resources.getString(R.string.confirmationCodeMessage),
            6
        )

        val phoneNumber = findViewById<EditText>(R.id.number)
        val password = findViewById<EditText>(R.id.password)
        val code = findViewById<EditText>(R.id.code)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            if (startGettingCode) {
                if (verificationCodeSender.isValidCode(code.text.toString())) finish()
                else Toast.makeText(this@LoginActivity, "Invalid code!", Toast.LENGTH_SHORT).show()
            } else verificationCodeSender.sendVerificationCode(phoneNumber.text.toString())

            startGettingCode = true
        }
    }
}