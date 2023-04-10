package ru.piece.of.crown.sveter.com

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationPhoneNumberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationPhoneNumberFragment : Fragment() {
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
            var outputPosition = sourcePosition + if (isDecrease) 0 else 1

            if (outputPosition == 11 || outputPosition == 8 || outputPosition == 4)
                outputPosition += if (isDecrease) -1 else 1

            return outputPosition
        }
    }

    lateinit var phoneNumberField: EditText
    lateinit var getVerificationCodeButton: Button

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

        getVerificationCodeButton.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
                ?.replace(R.id.container, RegistrationVerificationCodeFragment())
                ?.commit()
        }

        var ignoreAction = false

        phoneNumberField.doOnTextChanged { text, start, before, count ->
            if (ignoreAction) return@doOnTextChanged
            ignoreAction = true
            phoneNumberField.setText(formatPhoneNumber(text.toString()))
            phoneNumberField.setSelection(transformCursorPosition(start, before != 0))
            ignoreAction = false
        }
    }
}