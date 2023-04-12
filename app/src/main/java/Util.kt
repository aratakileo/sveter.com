import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import ru.piece.of.crown.sveter.com.R

object Util {
    fun getThemeType(context: Context): Int = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    fun isDarkThemeNow(context: Context): Boolean = getThemeType(context) == Configuration.UI_MODE_NIGHT_YES

    fun isLightThemeNow(context: Context): Boolean = getThemeType(context) == Configuration.UI_MODE_NIGHT_NO

    fun startActivityWithHorizontalSlideAnimation(context: Context, newActivity: Class<*>, isInAnimation: Boolean = true) {
        context.startActivity(Intent(context, newActivity))
        (context as Activity).apply{
            if (isInAnimation)
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
            else
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    fun finishActivityWithHorizontalSlideAnimation(context: Context, isOutAnimation: Boolean = true) {
        (context as Activity).apply{
            if (isOutAnimation)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            else
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)

            finish()
        }
    }
}