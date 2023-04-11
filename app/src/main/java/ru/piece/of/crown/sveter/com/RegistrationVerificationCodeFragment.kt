package ru.piece.of.crown.sveter.com

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
 * Use the [RegistrationVerificationCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationVerificationCodeFragment : Fragment() {
    lateinit var confirmationCodeField: EditText
    lateinit var applyConfirmationCodeButton: Button
    lateinit var applyConfirmationCodeButtonContainer: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration_verification_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmationCodeField = view.findViewById(R.id.confirmationCodeField)
        applyConfirmationCodeButton = view.findViewById(R.id.applyVerificationCodeButton)
        applyConfirmationCodeButtonContainer = view.findViewById(R.id.applyVerificationCodeButtonContainer)

        applyConfirmationCodeButtonContainer.apply {
            alpha = 0f
            translationY = 50f
        }
        applyConfirmationCodeButton.isEnabled = false

        confirmationCodeField.doOnTextChanged { text, start, before, count ->
            if (text?.length == activity?.resources?.getInteger(R.integer.confirmationCodeLength)) {
                if (!applyConfirmationCodeButton.isEnabled)
                    applyConfirmationCodeButtonContainer.apply {
                        applyConfirmationCodeButton.isEnabled = true
                        animate().alpha(1f).translationY(0f).duration = 1000
                    }
            } else if (applyConfirmationCodeButton.isEnabled)
                applyConfirmationCodeButtonContainer.apply {
                    applyConfirmationCodeButton.isEnabled = false
                    animate().alpha(0f).translationY(50f).duration = 1000
                }
        }

        applyConfirmationCodeButton.setOnClickListener {
            (activity as RegistrationActivity).apply {
                if (verificationCodeSender.isValidCode(confirmationCodeField.text.toString()))
                    showNextFragment(RegistrationPersonalDataFragment())
                else
                    Toast.makeText(this, R.string.invalidConfirmationCodeIssue, Toast.LENGTH_SHORT).show()
            }
        }
    }
}