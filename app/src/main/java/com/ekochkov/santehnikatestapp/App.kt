package com.ekochkov.santehnikatestapp

import android.app.Application
import com.ekochkov.santehnikatestapp.di.AppComponent
import com.ekochkov.santehnikatestapp.di.DaggerAppComponent
import com.ekochkov.santehnikatestapp.di.modules.RemoteModule
import com.ekochkov.santehnikatestapp.utils.API_KEYS.YANDEX_MAP_KIT_API_KEY
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(YANDEX_MAP_KIT_API_KEY)

        instance = this
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}