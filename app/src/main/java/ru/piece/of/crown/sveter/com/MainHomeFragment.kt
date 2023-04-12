package ru.piece.of.crown.sveter.com

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainHomeFragment : Fragment() {
    lateinit var userAvatar: ImageView
    lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = activity as MainActivity

        view.apply {
            userAvatar = findViewById(R.id.userAvatar)
            userName = findViewById(R.id.userName)

            findViewById<ImageView>(R.id.notificationsButton).apply {
                if (Util.isDarkThemeNow(context)) {
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
                if (Util.isDarkThemeNow(context)) {
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

        UserData.getUserFirstName(parentActivity).apply {
            if (this != null) userName.text = this
        }
    }

    companion object {
        const val ITEM_INDEX = 0
    }
}