package io.github.kaczmarek.delishki.ui.task.calendar

import android.os.Bundle
import android.view.View
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.presentation.task.calendar.TaskCalendarPresenter
import io.github.kaczmarek.delishki.ui.base.BaseActivity
import io.github.kaczmarek.delishki.ui.base.BaseView
import moxy.ktx.moxyPresenter

interface TaskCalendarView : BaseView

class TaskCalendarActivity : BaseActivity(R.layout.activity_calendar), TaskCalendarView,
    View.OnClickListener {
    private val presenter by moxyPresenter { TaskCalendarPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {

    }
}
