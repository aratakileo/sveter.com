import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.ImageView
import ru.piece.of.crown.sveter.com.R

val Context.themeType: Int
get() = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

val Context.isDarkThemeNow: Boolean
get() = themeType == Configuration.UI_MODE_NIGHT_YES

val Context.isLightThemeNow: Boolean
get() = themeType == Configuration.UI_MODE_NIGHT_NO

fun Context.startActivityWithHorizontalSlideAnimation(newActivity: Class<*>, isInAnimation: Boolean = true) {
    startActivity(Intent(this, newActivity))
    (this as Activity).apply{
        if (isInAnimation)
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        else
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

fun Context.finishActivityWithHorizontalSlideAnimation(isOutAnimation: Boolean = true) {
    (this as Activity).apply{
        if (isOutAnimation)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        else
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)

        finish()
    }
}

fun String.insert(index: Int, substring: String) = StringBuilder(this).apply { insert(index, substring) }.toString()

fun String.safeSubstring(startIndex: Int, endIndex: Int): String {
    if (this.isEmpty()) return this

    return substring(maxOf(0, startIndex), minOf(length, endIndex))
}

fun String.getOnlyDigits(
    maxNumberLength: Int? = null
): String {
    val outputText = filter { char: Char ->
        char in "1234567890"
    }

    if (maxNumberLength == null)
        return outputText

    return outputText.safeSubstring(0, maxNumberLength)
}

fun String.formatNumber(
    currentCursorPosition: Int,
    insertSubstringFilter: (Int, Int, Char) -> String?,
    maxNumberLength: Int? = null,
    removeLeftZeroes: Boolean = true
): Pair<String?, Int?> {
    var outputText = this.getOnlyDigits(maxNumberLength)

    if (removeLeftZeroes)
        outputText = outputText.trimStart('0')

    if (outputText.isEmpty()) return Pair(null, null)

    var outputCursorPosition = currentCursorPosition
    val textLength = outputText.length

    outputText.forEachIndexed { index, char ->
        insertSubstringFilter(index, index - textLength, char).apply {
            if (this@apply == null || this@apply.isEmpty()) return@forEachIndexed

            outputText = outputText.insert(index, this@apply)

            if (outputCursorPosition == index)
                outputCursorPosition += this@apply.length
        }
    }

    return Pair(outputText, minOf(outputCursorPosition, outputText.length))
}

fun ImageView.initFabAnimator() {
    alpha = 0f
    translationX = 50f
    isEnabled = false
}

fun ImageView.startFabAnimation(isShowingAnimation: Boolean) {
    if (isShowingAnimation && !isEnabled)
        animate().alpha(1f).translationX(0f).duration = 1000
    else if (!isShowingAnimation && isEnabled)
        animate().alpha(0f).translationX(50f).duration = 1000

    isEnabled = isShowingAnimation
}
