package io.github.kaczmarek.delishki.data.services.database.dao

import androidx.room.*
import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity
import io.github.kaczmarek.delishki.domain.task.entity.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAll(): List<TaskEntity>

    @Delete
    fun deleteTask(task: TaskEntity)

    @Query("SELECT COUNT(id) FROM tasks WHERE type = :tasksType")
    fun getCount(@Task.Companion.TaskType tasksType: String): Int
}