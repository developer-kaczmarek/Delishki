package io.github.kaczmarek.delishki.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.kaczmarek.delishki.data.services.database.RoomDatabase
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDatabaseService(context: Context): RoomDatabase {
        return RoomDatabase.getDatabase(context)
    }

}