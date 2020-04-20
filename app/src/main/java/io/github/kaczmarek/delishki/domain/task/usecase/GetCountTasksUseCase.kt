package io.github.kaczmarek.delishki.domain.task.usecase

import io.github.kaczmarek.delishki.data.repository.TaskRepository
import io.github.kaczmarek.delishki.domain.task.entity.Task

class GetCountTasksUseCase(private val repository: TaskRepository) {

    suspend fun getCountTasks(@Task.Companion.TaskType taskType: String): Int =
        repository.getCountTasks(taskType)
}