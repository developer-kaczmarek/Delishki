package io.github.kaczmarek.delishki.data.services.database.dao

import androidx.room.*
import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAll(): List<TaskEntity>

    @Delete
    fun deleteTask(task: TaskEntity)
}