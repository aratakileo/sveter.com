package ru.piece.of.crown.sveter.com

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class ChoosingOtherTravelParamentersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing_other_travel_paramenters)

        supportActionBar?.apply {
            if (Util.isDarkThemeNow(this@ChoosingOtherTravelParamentersActivity))
                setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.dark_green, theme)))

            subtitle = resources.getString(R.string.chooseOtherTravelParametersSubtitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeActionContentDescription(R.string.goBackAction)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Util.finishActivityWithHorizontalSlideAnimation(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return false
        }

        return false
    }
}