package ru.piece.of.crown.sveter.com

import ProposalData
import UserData
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.ozcanalasalvar.library.utils.DateUtils
import com.ozcanalasalvar.library.view.datePicker.DatePicker
import com.ozcanalasalvar.library.view.popup.DatePickerPopup
import com.ozcanalasalvar.library.view.popup.TimePickerPopup
import finishActivityWithHorizontalSlideAnimation
import formatNumber
import getOnlyDigits
import initFabAnimator
import isDarkThemeNow
import startActivityWithHorizontalSlideAnimation
import startFabAnimation

class ChoosingOtherTravelParametersActivity : AppCompatActivity() {
    private var pickedDate = -1L
    private var pickedHour = -1
    private var pickedMinute = -1
    private var passengersCount = 0
    private var tripCost = 0

    private lateinit var userRole: String
    private lateinit var pointOfDeparture: String
    private lateinit var pointOfArrival: String

    private lateinit var datePickerPopup: DatePickerPopup
    private lateinit var timePickerPopup: TimePickerPopup

    private lateinit var passengerCountField: TextView
    private lateinit var decreaseButton: ImageView
    private lateinit var increaseButton: ImageView

    private lateinit var departureDateField: EditText
    private lateinit var departureTimeField: EditText
    private lateinit var tripCostField: EditText

    private lateinit var publishButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing_other_travel_parameters)

        val currentDate = DateUtils.getCurrentTime()

        passengersCount = resources.getString(R.string.defaultPassengersCount).toInt()

        passengerCountField = findViewById(R.id.passengerCountField)
        decreaseButton = findViewById(R.id.decreaseButton)
        increaseButton = findViewById(R.id.increaseButton)

        departureDateField = findViewById(R.id.departureDateField)
        departureTimeField = findViewById(R.id.departureTimeField)
        tripCostField = findViewById(R.id.tripCostField)

        publishButton = findViewById(R.id.publishButton)

        tripCostField.setText(StringBuffer("0" + resources.getString(R.string.rubChar)))
        publishButton.initFabAnimator()

        userRole = intent.getStringExtra("role")!!
        pointOfDeparture = intent.getStringExtra("pointOfDeparture")!!
        pointOfArrival = intent.getStringExtra("pointOfArrival")!!

        publishButton.setOnClickListener {
            ProposalData.sendProposal(
                UserData.getPhoneNumber(this)!!,
                pointOfDeparture,
                pointOfArrival,
                pickedDate,
                departureTimeField.text.toString(),
                userRole,
                passengersCount,
                tripCost
            )
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
            finishAffinity()
        }

        datePickerPopup = DatePickerPopup.Builder()
            .from(this)
            .pickerMode(DatePicker.DAY_ON_FIRST)
            .textSize(20)
            .offset(3)
            .currentDate(currentDate)
            .listener { _, date, day, month, year ->
                pickedDate = date
                departureDateField.setText(StringBuffer("$day/${month + 1}/$year"))
                tryToShowOrHidePublishButton()
            }
            .build()

        timePickerPopup = TimePickerPopup.Builder()
            .from(this)
            .textSize(25)
            .offset(3)
            .setTime(DateUtils.getCurrentHour(), DateUtils.getCurrentMinute())
            .listener { _, hour, minute ->
                pickedHour = hour
                pickedMinute = minute

                departureTimeField.setText(StringBuffer("$hour:$minute"))
                tryToShowOrHidePublishButton()
            }
            .build()

        decreaseButton.setOnClickListener {
            passengersCount--
            passengersCount = maxOf(0, passengersCount)

            passengerCountField.text = passengersCount.toString()
        }

        increaseButton.setOnClickListener {
            passengersCount++
            passengersCount = minOf(4, passengersCount)

            passengerCountField.text = passengersCount.toString()
        }

        var ignoreAction = false

        tripCostField.doOnTextChanged { text, start, before, count ->
            if (ignoreAction) return@doOnTextChanged

            val newText = tripCostField.text.toString()

            ignoreAction = true
            val (newString, newCursorPosition) = newText.formatNumber(
                tripCostField.selectionStart,
                {_, negativeIndex, _ ->
                    if (negativeIndex == -3) return@formatNumber " "
                    return@formatNumber null
                },
                5
            )
            tripCostField.setText(StringBuffer((newString?:"0") + resources.getString(R.string.rubChar)))
            tripCostField.setSelection(newCursorPosition?:1)
            tripCost = newText.getOnlyDigits().ifEmpty{"0"}.toInt()
            ignoreAction = false

            tryToShowOrHidePublishButton()
        }

        departureDateField.setOnTouchListener { _, _ ->
            datePickerPopup.show()
            return@setOnTouchListener true
        }

        departureTimeField.setOnTouchListener { _, _ ->
            timePickerPopup.show()
            return@setOnTouchListener true
        }

        supportActionBar?.apply {
            if (isDarkThemeNow)
                setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_green, theme)))

            subtitle = resources.getString(R.string.chooseOtherTravelParametersSubtitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeActionContentDescription(R.string.goBackAction)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishActivityWithHorizontalSlideAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return false
        }

        return false
    }

    private fun tryToShowOrHidePublishButton() {
        publishButton.startFabAnimation(departureDateField.length() > 0 && departureTimeField.length() > 0 && tripCost > 5)
    }
}