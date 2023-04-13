package ru.piece.of.crown.sveter.com

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.ozcanalasalvar.library.utils.DateUtils
import com.ozcanalasalvar.library.view.datePicker.DatePicker
import com.ozcanalasalvar.library.view.popup.DatePickerPopup
import initFabAnimator
import startFabAnimation

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationUserDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationUserDataFragment : Fragment() {
    private var pickedDate = -1L

    private lateinit var datePickerPopup: DatePickerPopup

    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var dateOfBirthField: EditText
    private lateinit var apllyUserDataButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_user_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            firstNameField = findViewById(R.id.firstNameField)
            lastNameField = findViewById(R.id.lastNameField)
            dateOfBirthField = findViewById(R.id.dateOfBirthField)
            apllyUserDataButton = findViewById(R.id.applyUserDataButton)
        }

        val currentDate = DateUtils.getCurrentTime()

        datePickerPopup = DatePickerPopup.Builder()
            .from(requireActivity())
            .pickerMode(DatePicker.DAY_ON_FIRST)
            .textSize(20)
            .offset(3)
            .currentDate(currentDate)
            .listener { _, date, day, month, year ->
                pickedDate = date
                dateOfBirthField.setText(StringBuffer("$day/${month + 1}/$year"))
                tryToShowOrHideFinishRegistrationButton()
            }
            .build()

        firstNameField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()
        }
        lastNameField.doOnTextChanged { text, start, before, count ->
            tryToShowOrHideFinishRegistrationButton()
        }

        dateOfBirthField.setOnTouchListener { _, _ ->
            datePickerPopup.show()
            return@setOnTouchListener true
        }

        apllyUserDataButton.initFabAnimator()
        
        apllyUserDataButton.setOnClickListener {
            (activity as RegistrationActivity).apply {
                userFirstName = firstNameField.text.toString()
                userLastName = lastNameField.text.toString()
                userDateOfBirth = dateOfBirthField.text.toString()
                showNextFragment(RegistrationPasswordFragment())
            }
        }
    }

    private fun tryToShowOrHideFinishRegistrationButton() {
        apllyUserDataButton.startFabAnimation(
            firstNameField.length() >= 2 && lastNameField.length() >= 2 && dateOfBirthField.length() >= 0
        )
    }
}