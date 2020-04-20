package io.github.kaczmarek.delishki.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.domain.task.entity.Task
import io.github.kaczmarek.delishki.presentation.main.MainPresenter
import io.github.kaczmarek.delishki.ui.base.BaseActivity
import io.github.kaczmarek.delishki.ui.base.BaseView
import io.github.kaczmarek.delishki.ui.task.details.TaskDetailsActivity
import io.github.kaczmarek.delishki.ui.task.list.TaskListActivity
import io.github.kaczmarek.delishki.ui.task.list.TaskListActivity.Companion.KEY_TYPE_TASKS
import kotlinx.android.synthetic.main.activity_main.*
import moxy.ktx.moxyPresenter
import java.text.SimpleDateFormat
import java.util.*

interface MainView : BaseView {
    fun showCountTasks(
        countTodayTasks: Int,
        countPlanTasks: Int,
        countAnytimeTasks: Int,
        countSomedayTasks: Int
    )
}

class MainActivity : BaseActivity(R.layout.activity_main), MainView, View.OnClickListener {
    private val presenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        title = getGreetingMessage()
        tv_main_current_date.text = getDateTime()
        cv_main_today_tasks.setOnClickListener(this)
        cv_main_plan_tasks.setOnClickListener(this)
        cv_main_anytime_tasks.setOnClickListener(this)
        cv_main_someday_tasks.setOnClickListener(this)
        fab_main_add_task.setOnClickListener(this)
        presenter.getCountTasks()
    }

    private fun getGreetingMessage(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> getString(R.string.activity_main_good_morning)
            in 12..15 -> getString(R.string.activity_main_good_afternoon)
            in 16..20 -> getString(R.string.activity_main_good_evening)
            in 21..23 -> getString(R.string.activity_main_good_night)
            else -> getString(R.string.activity_main_hello)
        }
    }

    private fun getDateTime(): String? {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cv_main_today_tasks -> startActivity(
                Intent(this, TaskListActivity::class.java).apply {
                    putExtra(KEY_TYPE_TASKS, Task.TODAY)
                })
            R.id.cv_main_anytime_tasks -> startActivity(
                Intent(
                    this,
                    TaskListActivity::class.java
                ).apply {
                    putExtra(KEY_TYPE_TASKS, Task.ANYTIME)
                })
            R.id.cv_main_someday_tasks -> startActivity(
                Intent(
                    this,
                    TaskListActivity::class.java
                ).apply {
                    putExtra(KEY_TYPE_TASKS, Task.SOMEDAY)
                })
            R.id.cv_main_plan_tasks -> startActivity(
                Intent(
                    this,
                    TaskListActivity::class.java
                ).apply {
                    putExtra(KEY_TYPE_TASKS, Task.PLANS)
                })
            R.id.fab_main_add_task -> startActivity(Intent(this, TaskDetailsActivity::class.java))
        }
    }

    override fun showCountTasks(
        countTodayTasks: Int,
        countPlanTasks: Int,
        countAnytimeTasks: Int,
        countSomedayTasks: Int
    ) {
        tv_main_count_today_tasks.text = resources.getQuantityString(
            R.plurals.common_tasks,
            countTodayTasks,
            countTodayTasks
        )
        tv_main_count_plan_tasks.text = resources.getQuantityString(
            R.plurals.common_tasks,
            countPlanTasks,
            countPlanTasks
        )
        tv_main_count_anytime_tasks.text = resources.getQuantityString(
            R.plurals.common_tasks,
            countAnytimeTasks,
            countAnytimeTasks
        )
        tv_main_count_someday_tasks.text = resources.getQuantityString(
            R.plurals.common_tasks,
            countSomedayTasks,
            countSomedayTasks
        )
    }
}
