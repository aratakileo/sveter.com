package ru.piece.of.crown.sveter.com

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationVerificationCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationVerificationCodeFragment : Fragment() {
    lateinit var confirmationCodeField: EditText
    lateinit var applyConfirmationCodeButton: ImageView

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

        applyConfirmationCodeButton.apply {
            alpha = 0f
            translationX = 50f
            isEnabled = false
        }

        confirmationCodeField.doOnTextChanged { text, start, before, count ->
            if (text?.length == requireActivity().resources.getInteger(R.integer.confirmationCodeLength)) {
                if (!applyConfirmationCodeButton.isEnabled)
                    applyConfirmationCodeButton.apply {
                        isEnabled = true
                        animate().alpha(1f).translationX(0f).duration = 1000
                    }
            } else if (applyConfirmationCodeButton.isEnabled)
                applyConfirmationCodeButton.apply {
                    isEnabled = false
                    animate().alpha(0f).translationX(50f).duration = 1000
                }
        }

        applyConfirmationCodeButton.setOnClickListener {
            (activity as RegistrationActivity).apply {
                if (verificationCodeSender.isValidCode(confirmationCodeField.text.toString())) {
                    if (isRegistrationProcess)
                        showNextFragment(RegistrationUserDataFragment())
                    else
                        showNextFragment(RegistrationPasswordFragment())
                }
                else
                    Toast.makeText(this, R.string.invalidConfirmationCodeIssue, Toast.LENGTH_SHORT).show()
            }
        }
    }
}