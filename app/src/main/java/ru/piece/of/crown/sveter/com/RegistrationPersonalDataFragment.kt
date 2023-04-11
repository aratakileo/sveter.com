package ru.piece.of.crown.sveter.com

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.widget.doOnTextChanged

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationPersonalDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationPersonalDataFragment : Fragment() {
    lateinit var firstNameField: EditText
    lateinit var lastNameField: EditText
    lateinit var dateOfBirthField: EditText
    lateinit var finishRegistrationButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_personal_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            firstNameField = findViewById(R.id.firstNameField)
            lastNameField = findViewById(R.id.lastNameField)
            dateOfBirthField = findViewById(R.id.dateOfBirthField)
            finishRegistrationButton = findViewById(R.id.applyUserDataButton)
        }

        var ignoreAction = false

        firstNameField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()
        }
        lastNameField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()
        }
        dateOfBirthField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()

            if (ignoreAction) return@doOnTextChanged
            ignoreAction = true
            val textString = text.toString()
            val cursorPositionDecrease = textString.length - textString.replace(".", "").length
            dateOfBirthField.setText(formatData(textString))
            dateOfBirthField.setSelection(
                transformCursorPosition(
                    start,
                    before != 0
                ) - cursorPositionDecrease)
            ignoreAction = false
        }

        finishRegistrationButton.apply {
            isEnabled = false
            alpha = 0f
            translationX = 50f
        }
        
        finishRegistrationButton.setOnClickListener {
            (activity as RegistrationActivity).apply {
                userFirstName = firstNameField.text.toString()
                userLastName = lastNameField.text.toString()
                userDateOfBirth = dateOfBirthField.text.toString()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun tryToShowOrHideFinishRegistrationButton() {
        if (firstNameField.length() >= 2 && lastNameField.length() >= 2 && dateOfBirthField.length() >= 10) {
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

    companion object {
        private fun cleanToNumber(sourceNumber: String): String {
            var outputNumber =
                sourceNumber.filter { char: Char ->
                    char in "1234567890"
                }

            if (outputNumber.length > 8)
                outputNumber = outputNumber.substring(0, 8)

            return outputNumber
        }

        private fun formatData(sourceNumber: String): String {
            var outputNumber = cleanToNumber(sourceNumber)

            if (outputNumber.length > 4)
                outputNumber = java.lang.StringBuilder(outputNumber).apply { insert(4, '/') }.toString()

            if (outputNumber.length > 2)
                outputNumber = java.lang.StringBuilder(outputNumber).apply { insert(2, '/') }.toString()

            return outputNumber
        }

        private fun transformCursorPosition(sourcePosition: Int, isDecrease: Boolean = false): Int {
            var outputPosition = sourcePosition + if (isDecrease || sourcePosition == 10) 0 else 1

            if (outputPosition == 6 || outputPosition == 3)
                outputPosition += if (isDecrease) -1 else 1

            return outputPosition
        }
    }
}