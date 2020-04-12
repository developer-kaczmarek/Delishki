package io.github.kaczmarek.delishki.data.services.database

import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity

class MockDatabase : IDatabase {

    override suspend fun getTasks(): List<TaskEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: TaskEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getCountTasks(taskType: String): Int {

        return when (taskType) {
            "PLANS" -> 11
            "TODAY" -> 5
            "ANYTIME" -> 7
            else -> 34
        }
    }
}