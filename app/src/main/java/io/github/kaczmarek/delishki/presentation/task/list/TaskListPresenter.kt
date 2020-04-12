package io.github.kaczmarek.delishki.presentation.task.list

import io.github.kaczmarek.delishki.di.DIManager
import io.github.kaczmarek.delishki.domain.task.usecase.GetCountTasksUseCase
import io.github.kaczmarek.delishki.presentation.base.BasePresenter
import io.github.kaczmarek.delishki.ui.task.list.TaskListView
import io.github.kaczmarek.delishki.util.logError
import kotlinx.coroutines.launch
import moxy.presenterScope
import javax.inject.Inject

class TaskListPresenter : BasePresenter<TaskListView>() {
    @Inject
    lateinit var getCountTasksUseCase: GetCountTasksUseCase

    init {
        DIManager.getTaskListSubcomponent().inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DIManager.removeTaskListSubcomponent()
    }

    fun getCountTasks(taskType: String) {
        presenterScope.launch {
            try {
                val count = getCountTasksUseCase.getCountTasks(taskType)
                viewState.showCountTasks(count)
            } catch (e: Exception) {
                logError(TAG, e.message)
            }
        }
    }
    companion object {
        private const val TAG = "TaskListPresenter"
    }
}