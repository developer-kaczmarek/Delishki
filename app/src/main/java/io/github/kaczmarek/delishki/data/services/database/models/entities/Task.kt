package io.github.kaczmarek.delishki.data.services.database.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.kaczmarek.delishki.domain.task.entity.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    val title: String,
    var description: String?,
    var isCompleted: Boolean,
    @Task.Companion.TaskType
    val type: String,
    val date: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    
}