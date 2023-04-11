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

        view.apply {
            finishRegistrationButton = findViewById(R.id.finishRegistrationButton)
            passwordField = findViewById(R.id.passwordField)
            passwordConfirmationField = findViewById(R.id.passwordConfirmationField)
        }

        finishRegistrationButton.setOnClickListener {
            if (passwordField.text.toString() == passwordConfirmationField.text.toString())
                (activity as RegistrationActivity).apply {
                    UserData.registrateUser(
                        this,
                        userFirstName,
                        userLastName,
                        userDateOfBirth,
                        userNumber,
                        passwordField.text.toString()
                    )
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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
        if (passwordField.length() >= 8 && passwordConfirmationField.length() == passwordField.length()) {
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