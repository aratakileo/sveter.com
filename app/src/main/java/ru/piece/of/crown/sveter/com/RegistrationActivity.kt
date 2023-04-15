package ru.piece.of.crown.sveter.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.teacondemns.smsru.codesender.api.CodeSender

open class RegistrationActivity(val isRegistrationProcess: Boolean = true) : AppCompatActivity() {
    lateinit var verificationCodeSender: CodeSender
    lateinit var userPhoneNumber: String
    lateinit var userFirstName: String
    lateinit var userLastName: String
    lateinit var userDateOfBirth: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.light_orange, theme)

        verificationCodeSender = CodeSender(resources.getInteger(R.integer.confirmationCodeLength))
        verificationCodeSender.setToken(this, R.string.smsRuAPIToken)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().add(R.id.container, RegistrationPhoneNumberFragment()).commit()
    }

    override fun onBackPressed() {super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    fun showNextFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
            .replace(R.id.container, fragment)
            .commit()
    }
}