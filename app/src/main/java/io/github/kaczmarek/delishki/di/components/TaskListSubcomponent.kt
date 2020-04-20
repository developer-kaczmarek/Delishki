package io.github.kaczmarek.delishki.di.components

import dagger.Subcomponent
import io.github.kaczmarek.delishki.di.modules.TaskListModule
import io.github.kaczmarek.delishki.di.scopes.MainScope
import io.github.kaczmarek.delishki.presentation.task.list.TaskListPresenter

@MainScope
@Subcomponent(modules = [TaskListModule::class])
interface TaskListSubcomponent {
    fun inject(presenter: TaskListPresenter)
}