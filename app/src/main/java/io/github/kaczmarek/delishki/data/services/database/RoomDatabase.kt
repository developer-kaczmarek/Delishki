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
abstract class RoomDatabase : RoomDatabase(), IDatabase {

    abstract fun taskDao(): TaskDao

    override suspend fun getTasks(): List<TaskEntity> = taskDao().getAll()

    override suspend fun saveTask(task: TaskEntity) {
        taskDao().save(task)
    }

    companion object {

        private const val DB_NAME = "delishki_db"

        fun getDatabase(context: Context): io.github.kaczmarek.delishki.data.services.database.RoomDatabase {

            synchronized(this) {
                return Room.databaseBuilder(
                    context,
                    io.github.kaczmarek.delishki.data.services.database.RoomDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}