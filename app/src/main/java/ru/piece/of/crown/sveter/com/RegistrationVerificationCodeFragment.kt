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
import initFabAnimator
import startFabAnimation

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

        applyConfirmationCodeButton.initFabAnimator()

        confirmationCodeField.doOnTextChanged { text, start, before, count ->
            applyConfirmationCodeButton.startFabAnimation(text?.length == requireActivity().resources.getInteger(R.integer.confirmationCodeLength))
        }

        applyConfirmationCodeButton.setOnClickListener {
            (activity as RegistrationActivity).apply {
                if (verificationCodeSender.tryToConfirmSessionByCode(confirmationCodeField.text.toString())) {
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