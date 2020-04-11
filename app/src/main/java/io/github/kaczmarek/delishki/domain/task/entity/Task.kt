package io.github.kaczmarek.delishki.domain.task.entity

import androidx.annotation.StringDef
import io.github.kaczmarek.delishki.data.services.database.models.entities.TaskEntity

data class Task(
    val title: String,
    var description: String?,
    var isCompleted: Boolean,
    @TaskType
    val type: String,
    val date: String?
) {

    constructor(task: TaskEntity) : this(
        task.title,
        task.description,
        task.isCompleted,
        task.type,
        task.date
    )

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(PLANS, TODAY, ANYTIME, SOMEDAY)
        annotation class TaskType

        const val PLANS = "PLANS"
        const val TODAY = "TODAY"
        const val ANYTIME = "ANYTIME"
        const val SOMEDAY = "SOMEDAY"
    }
}