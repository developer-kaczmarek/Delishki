package io.github.kaczmarek.delishki.data.services.database

import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity

interface IDatabase {

    suspend fun getTasks(): List<TaskEntity>

    suspend fun saveTask(task: TaskEntity)

    suspend fun getCountTasks(taskType: String): Int

}