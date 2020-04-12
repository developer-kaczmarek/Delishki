package io.github.kaczmarek.delishki

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import io.github.kaczmarek.delishki.di.DIManager
import io.github.kaczmarek.delishki.di.components.DaggerAppComponent
import io.github.kaczmarek.delishki.util.Utils

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        DIManager.appComponent = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()

        Stetho.initializeWithDefaults(applicationContext)

        Utils.init(this)

        AndroidThreeTen.init(this)
    }
}