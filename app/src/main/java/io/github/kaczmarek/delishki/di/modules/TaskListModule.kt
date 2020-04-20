package io.github.kaczmarek.delishki.di.modules

import dagger.Module
import dagger.Provides
import io.github.kaczmarek.delishki.data.repository.TaskMapper
import io.github.kaczmarek.delishki.data.repository.TaskRepository
import io.github.kaczmarek.delishki.data.services.database.MockDatabase
import io.github.kaczmarek.delishki.data.services.database.RoomDatabase
import io.github.kaczmarek.delishki.di.scopes.MainScope
import io.github.kaczmarek.delishki.domain.task.usecase.GetCountTasksUseCase

@Module
object TaskListModule {

    @Provides
    @MainScope
    fun provideTaskMapper(): TaskMapper = TaskMapper()

    @Provides
    @MainScope
    fun provideTaskRepository(
        database: RoomDatabase,
        taskMapper: TaskMapper
    ): TaskRepository = TaskRepository(MockDatabase(), taskMapper)


    @Provides
    @MainScope
    fun provideGetCountTasksUseCase(taskRepository: TaskRepository): GetCountTasksUseCase =
        GetCountTasksUseCase(taskRepository)

}