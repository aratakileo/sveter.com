package ru.piece.of.crown.sveter.com

import ProposalData
import UserData
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ozcanalasalvar.library.utils.DateUtils
import insert
import isDarkThemeNow

class MainHomeFragment : Fragment() {
    private lateinit var driverTicketContainer: LinearLayout
    private lateinit var passengerTicketContainer: LinearLayout
    private lateinit var userAvatar: ImageView
    private lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = activity as MainActivity

        view.apply {
            userAvatar = findViewById(R.id.userAvatar)
            userName = findViewById(R.id.userName)
            driverTicketContainer = findViewById(R.id.driverTicketContainer)
            passengerTicketContainer = findViewById(R.id.passengerTicketContainer)

            findViewById<ImageView>(R.id.notificationsButton).apply {
                if (context.isDarkThemeNow) {
                    background = resources.getDrawable(
                        R.drawable.ripple_animation_light_orange_circle,
                        parentActivity.theme
                    )
                    setColorFilter(resources.getColor(R.color.light_orange, parentActivity.theme))
                }

                setOnClickListener {
                    parentActivity.navigationBarController.selectItem(MainInboxFragment.ITEM_INDEX)
                }
            }

            findViewById<LinearLayout>(R.id.profileCard).apply {
                if (context.isDarkThemeNow) {
                    background = resources.getDrawable(
                        R.drawable.ripple_animation_light_orange_rounded_square,
                        parentActivity.theme
                    )
                }
                setOnClickListener {
                    parentActivity.navigationBarController.selectItem(MainProfileFragment.ITEM_INDEX)
                }
            }

            findViewById<LinearLayout>(R.id.runNewTravelButton).setOnClickListener {
                parentActivity.navigationBarController.selectItem(MainTravelFragment.ITEM_INDEX)
            }
        }

        ProposalData.getProposalsData()?.forEach {
            parseTicket(parentActivity, it, it.getString("role")!!)
        }

        UserData.getUserFirstName(parentActivity).apply {
            if (this != null) userName.text = this
        }
    }

    private fun parseTicket(context: Context, ticketData: QueryDocumentSnapshot, role: String) {
        val ticket = LayoutInflater.from(context).inflate(R.layout.ticket, null).apply {
            val authorData = UserData.getUserDataByPhoneNumber(ticketData.getString("authorNumber"))

            if (role == "passenger")
                findViewWithTag<ImageView>("proposalIcon").setImageResource(R.drawable.profile_icon)

            findViewWithTag<TextView>("userName").text = authorData?.getString("firstName")
            findViewWithTag<TextView>("from").text = ticketData.getString("pointOfDeparture")
            findViewWithTag<TextView>("to").text = ticketData.getString("pointOfArrival")
            ticketData.get("departureDate").apply {
                val value = this.toString().toLong()
                findViewWithTag<TextView>("date").text = StringBuffer("${DateUtils.getDay(value)} ${
                    when (DateUtils.getMonth(value)) {
                        1 -> "янв."
                        2 -> "фев."
                        3 -> "мар."
                        4 -> "апр."
                        5 -> "мая"
                        6 -> "июн."
                        7 -> "июл."
                        8 -> "авг."
                        9 -> "сен."
                        10 -> "окт."
                        11 -> "ноя."
                        else -> "дек. "
                    }
                } ${DateUtils.getYear(value)}")
            }
            findViewWithTag<TextView>("time").text = ticketData.getString("departureTime")

            var tripCostString = ticketData.get("tripCost").toString()

            if (tripCostString.length > 3)
                tripCostString = tripCostString.insert(-3, " ")

            findViewWithTag<TextView>("cost").text = StringBuffer(tripCostString + resources.getString(R.string.rubChar))
        }
        if (role == "driver")
            driverTicketContainer.addView(ticket)
        else
            passengerTicketContainer.addView(ticket)
    }

    companion object {
        const val ITEM_INDEX = 0
    }
}