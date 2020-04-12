package io.github.kaczmarek.delishki.presentation.main

import io.github.kaczmarek.delishki.di.DIManager
import io.github.kaczmarek.delishki.domain.task.entity.Task.Companion.TODAY
import io.github.kaczmarek.delishki.domain.task.entity.Task.Companion.ANYTIME
import io.github.kaczmarek.delishki.domain.task.entity.Task.Companion.PLANS
import io.github.kaczmarek.delishki.domain.task.entity.Task.Companion.SOMEDAY
import io.github.kaczmarek.delishki.domain.task.usecase.GetCountTasksUseCase
import io.github.kaczmarek.delishki.presentation.base.BasePresenter
import io.github.kaczmarek.delishki.ui.main.MainView
import io.github.kaczmarek.delishki.util.logError
import kotlinx.coroutines.launch
import javax.inject.Inject
import moxy.presenterScope

class MainPresenter : BasePresenter<MainView>() {
    @Inject
    lateinit var getCountTasksUseCase: GetCountTasksUseCase

    init {
        DIManager.getMainSubcomponent().inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DIManager.removeMainSubcomponent()
    }

    fun getCountTasks() {
        presenterScope.launch {
            try {
                val countTodayTasks = getCountTasksUseCase.getCountTasks(TODAY)
                val countPlanTasks = getCountTasksUseCase.getCountTasks(PLANS)
                val countAnytimeTasks = getCountTasksUseCase.getCountTasks(ANYTIME)
                val countSomedayTasks = getCountTasksUseCase.getCountTasks(SOMEDAY)
                viewState.showCountTasks(countTodayTasks, countPlanTasks, countAnytimeTasks, countSomedayTasks)
            } catch (e: Exception) {
                logError(TAG, e.message)
            }
        }
    }

    companion object {
        private const val TAG = "MainPresenter"
    }
}