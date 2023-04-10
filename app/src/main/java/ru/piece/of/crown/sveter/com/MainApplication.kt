package ru.piece.of.crown.sveter.com

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(resources.getString(R.string.yandexMapAPIToken))
    }
}