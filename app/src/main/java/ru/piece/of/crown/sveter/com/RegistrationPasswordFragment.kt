package ru.piece.of.crown.sveter.com

import UserData
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged

class RegistrationPasswordFragment : Fragment() {
    lateinit var finishRegistrationButton: ImageView
    lateinit var passwordField: EditText
    lateinit var passwordConfirmationField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = activity as RegistrationActivity

        view.apply {
            finishRegistrationButton = findViewById(R.id.finishRegistrationButton)
            passwordField = findViewById(R.id.passwordField)
            passwordConfirmationField = findViewById(R.id.passwordConfirmationField)

            if (!parentActivity.isRegistrationProcess) {
                findViewById<TextView>(R.id.activityTitle).setText(R.string.insertLoginPasswordTitle)
                findViewById<TextView>(R.id.activitySubtitle).setText(R.string.insertLoginPasswordSubtitle)

                passwordConfirmationField.visibility = View.GONE
            }
        }

        finishRegistrationButton.setOnClickListener {
            if (passwordField.text.toString() == passwordConfirmationField.text.toString() || !parentActivity.isRegistrationProcess)
                parentActivity.apply {
                    val password = passwordField.text.toString()
                    var isPossibleToFinish = true

                    if (isRegistrationProcess)
                        UserData.registrateUser(
                            this,
                            userFirstName,
                            userLastName,
                            userDateOfBirth,
                            userPhoneNumber,
                            password
                        )
                    else if (!UserData.isPasswordCorrect(userPhoneNumber, password)) {
                        isPossibleToFinish = false
                        Toast.makeText(this, R.string.invalidPasswordIssue, Toast.LENGTH_SHORT)
                            .show()
                    } else
                        UserData.loginUser(this, userPhoneNumber, password)

                    if (isPossibleToFinish) {
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
                        finish()
                    }
                }
            else
                Toast.makeText(activity, R.string.differentPasswordsIssue, Toast.LENGTH_SHORT).show()
        }

        passwordField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()
        }

        passwordConfirmationField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()
        }
    }

    private fun tryToShowOrHideFinishRegistrationButton() {
        if (passwordField.length() >= 8 && (passwordConfirmationField.length() == passwordField.length() || !(activity as RegistrationActivity).isRegistrationProcess)) {
            if (!finishRegistrationButton.isEnabled)
                finishRegistrationButton.apply {
                    isEnabled = true
                    animate().alpha(1f).translationX(0f).duration = 1000
                }
        } else if (finishRegistrationButton.isEnabled)
            finishRegistrationButton.apply {
                isEnabled = false
                animate().alpha(0f).translationX(50f).duration = 1000
            }
    }
}