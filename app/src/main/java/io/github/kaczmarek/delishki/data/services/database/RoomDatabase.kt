package io.github.kaczmarek.delishki.data.services.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.kaczmarek.delishki.data.services.database.dao.TaskDao
import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity

@Database(
    entities = [
        TaskEntity::class
    ]
    , version = 1
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        private const val DB_NAME = "delishki_db"

        fun getDatabase(context: Context): DatabaseService {

            synchronized(this) {
                return Room.databaseBuilder(context, DatabaseService::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}