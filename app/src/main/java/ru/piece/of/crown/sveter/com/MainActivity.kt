package ru.piece.of.crown.sveter.com

import NavigationBarController
import UserData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var navigationBarController: NavigationBarController

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!UserData.isUserAlreadyLogin(this)) {
            startActivity(Intent(this, SelectLoginOrRegistrationActivity::class.java))
            finish()
        } else
            println("Login with phone number: ${UserData.getPhoneNumber(this)}")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val primaryColorTypedValue = TypedValue()

        supportActionBar?.hide()
        theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, TypedValue(), true)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = primaryColorTypedValue.data

        navigationBarController = NavigationBarController(this, R.id.container).apply {
            addItem(MainHomeFragment(), findViewById(R.id.firstItemIcon), findViewById(R.id.firstItemTitle))
            addItem(MainTravelFragment(), findViewById(R.id.secondItemIcon), findViewById(R.id.secondItemTitle))
            addItem(MainInboxFragment(), findViewById(R.id.thirdItemIcon), findViewById(R.id.thirdItemTitle))
            addItem(MainProfileFragment(), findViewById(R.id.fourthItemIcon), findViewById(R.id.fourthItemTitle))
            init()

            setOnSelectListener { views, index ->
                resources.getColor(R.color.fresh_orange, theme).apply {
                    (views[0] as ImageView).setColorFilter(this)
                    (views[1] as TextView).setTextColor(this)
                }
            }

            setOnUnselectListener { views, index ->
                resources.getColor(R.color.dirty_grey, theme).apply {
                    (views[0] as ImageView).setColorFilter(this)
                    (views[1] as TextView).setTextColor(this)
                }
            }
        }

        findViewById<LinearLayout>(R.id.firstItem).setOnClickListener {
            navigationBarController.selectItem(MainHomeFragment.ITEM_INDEX)
        }

        findViewById<LinearLayout>(R.id.secondItem).setOnClickListener {
            navigationBarController.selectItem(MainTravelFragment.ITEM_INDEX)
        }

        findViewById<LinearLayout>(R.id.thirdItem).setOnClickListener {
            navigationBarController.selectItem(MainInboxFragment.ITEM_INDEX)
        }

        findViewById<LinearLayout>(R.id.fourthItem).setOnClickListener {
            navigationBarController.selectItem(MainProfileFragment.ITEM_INDEX)
        }
    }
}
