package ru.piece.of.crown.sveter.com

import UserData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doOnTextChanged

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationPhoneNumberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationPhoneNumberFragment : Fragment() {
    lateinit var phoneNumberField: EditText
    lateinit var getVerificationCodeButton: Button
    lateinit var getVerificationCodeButtonContainer: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration_phone_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumberField = view.findViewById(R.id.phoneNumberField)
        getVerificationCodeButton = view.findViewById(R.id.getVerificationCodeButton)
        getVerificationCodeButtonContainer = view.findViewById(R.id.getVerificationCodeButtonContainer)

        getVerificationCodeButtonContainer.apply {
            alpha = 0f
            translationY = 50f
        }
        getVerificationCodeButton.isEnabled = false

        getVerificationCodeButton.setOnClickListener {
            (activity as RegistrationActivity).apply {
                userNumber = "7${cleanPhoneNumber(phoneNumberField.text.toString())}"

                if (UserData.hasUserWithPhoneNumber(userNumber))
                    Toast.makeText(this, R.string.phoneNumberDuplicationIssue, Toast.LENGTH_LONG).show()
                else
                    showNextFragment(RegistrationVerificationCodeFragment())
            }
        }

        var ignoreAction = false

        phoneNumberField.doOnTextChanged { text, start, before, count ->
            if (text?.length == 13) {
                if (!getVerificationCodeButton.isEnabled)
                    getVerificationCodeButtonContainer.apply {
                        getVerificationCodeButton.isEnabled = true
                        animate().alpha(1f).translationY(0f).duration = 1000
                    }
            } else if (getVerificationCodeButton.isEnabled)
                getVerificationCodeButtonContainer.apply {
                    getVerificationCodeButton.isEnabled = false
                    animate().alpha(0f).translationY(50f).duration = 1000
                }
            
            if (ignoreAction) return@doOnTextChanged
            ignoreAction = true
            val textString = text.toString()
            val cursorPositionDecrease = textString.length - textString.replace(".", "").length
            phoneNumberField.setText(formatPhoneNumber(textString))
            phoneNumberField.setSelection(transformCursorPosition(start, before != 0) - cursorPositionDecrease)
            ignoreAction = false
        }
    }

    companion object {
        private fun cleanPhoneNumber(sourceNumber: String): String {
            var outputNumber =
                sourceNumber.filter { char: Char ->
                    char in "1234567890"
                }

            if (outputNumber.length > 10)
                outputNumber = outputNumber.substring(0, 10)

            return outputNumber
        }

        private fun formatPhoneNumber(sourceNumber: String): String {
            var outputNumber = cleanPhoneNumber(sourceNumber)

            if (outputNumber.length > 8)
                outputNumber = java.lang.StringBuilder(outputNumber).apply { insert(8, '-') }.toString()

            if (outputNumber.length > 6)
                outputNumber = java.lang.StringBuilder(outputNumber).apply { insert(6, '-') }.toString()

            if (outputNumber.length > 3)
                outputNumber = java.lang.StringBuilder(outputNumber).apply { insert(3, ' ') }.toString()

            return outputNumber
        }

        private fun transformCursorPosition(sourcePosition: Int, isDecrease: Boolean = false): Int {
            var outputPosition = sourcePosition + if (isDecrease || sourcePosition == 13) 0 else 1

            if (outputPosition == 11 || outputPosition == 8 || outputPosition == 4)
                outputPosition += if (isDecrease) -1 else 1

            return outputPosition
        }
    }
}