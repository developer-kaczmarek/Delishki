package io.github.kaczmarek.delishki.di.services.database.dao

import androidx.room.*
import io.github.kaczmarek.delishki.di.services.database.models.entities.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(task: Task)

    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task>

    @Delete
    fun deleteTask(task: Task)
}