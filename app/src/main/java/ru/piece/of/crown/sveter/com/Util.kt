package ru.piece.of.crown.sveter.com

import android.content.Context
import android.content.res.Configuration

object Util {
    fun getThemeType(context: Context): Int = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    fun isDarkThemeNow(context: Context): Boolean = getThemeType(context) == Configuration.UI_MODE_NIGHT_YES

    fun isLightThemeNow(context: Context): Boolean = getThemeType(context) == Configuration.UI_MODE_NIGHT_NO
}