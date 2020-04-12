package io.github.kaczmarek.delishki.di

import io.github.kaczmarek.delishki.di.components.AppComponent
import io.github.kaczmarek.delishki.di.components.MainSubcomponent
import io.github.kaczmarek.delishki.di.components.TaskListSubcomponent
import io.github.kaczmarek.delishki.di.modules.MainModule
import io.github.kaczmarek.delishki.di.modules.TaskListModule

object DIManager {

    lateinit var appComponent: AppComponent
    private var mainSubcomponent: MainSubcomponent? = null
    private var taskListSubcomponent: TaskListSubcomponent? = null

    fun removeMainSubcomponent() {
        mainSubcomponent = null
    }

    fun getMainSubcomponent(): MainSubcomponent {
        if (mainSubcomponent == null) {
            mainSubcomponent = appComponent.addMainSubcomponent(MainModule)
        }
        return mainSubcomponent ?: throw IllegalStateException("$mainSubcomponent must no be null")
    }

    fun removeTaskListSubcomponent() {
        taskListSubcomponent = null
    }

    fun getTaskListSubcomponent(): TaskListSubcomponent {
        if (taskListSubcomponent == null) {
            taskListSubcomponent = appComponent.addTaskListSubcomponent(TaskListModule)
        }
        return taskListSubcomponent
            ?: throw IllegalStateException("$taskListSubcomponent must no be null")
    }

}