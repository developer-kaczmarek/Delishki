package io.github.kaczmarek.delishki.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.kaczmarek.delishki.di.modules.AppModule
import io.github.kaczmarek.delishki.data.services.database.RoomDatabase
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(roomDatabase: RoomDatabase)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}