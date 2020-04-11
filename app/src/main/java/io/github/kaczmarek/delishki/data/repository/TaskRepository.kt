package io.github.kaczmarek.delishki.data.repository

import io.github.kaczmarek.delishki.data.services.database.IDatabase
import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity
import io.github.kaczmarek.delishki.domain.task.entity.Task

class TaskRepository(
    private val database: IDatabase,
    private val mapper: ITaskMapper
) {
    suspend fun getTasks(): List<Task> = database.getTasks().map { mapper.map(it) }

    suspend fun saveTask(task: Task) {
        database.saveTask(mapper.map(task))
    }
}

interface ITaskMapper {

    fun map(dbObject: TaskEntity): Task

    fun map(obj: Task): TaskEntity
}

class TaskMapper : ITaskMapper {

    override fun map(dbObject: TaskEntity) = Task(dbObject)

    override fun map(obj: Task) = TaskEntity(obj)
}