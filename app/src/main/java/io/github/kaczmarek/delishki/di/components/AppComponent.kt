package io.github.kaczmarek.delishki.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.kaczmarek.delishki.di.modules.AppModule
import io.github.kaczmarek.delishki.di.modules.MainModule
import io.github.kaczmarek.delishki.di.modules.TaskListModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun addMainSubcomponent(module: MainModule): MainSubcomponent
    fun addTaskListSubcomponent(module: TaskListModule): TaskListSubcomponent

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}