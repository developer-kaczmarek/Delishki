package io.github.kaczmarek.delishki.ui.task.list


import android.os.Bundle
import android.view.View
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.domain.task.entity.Task
import io.github.kaczmarek.delishki.ui.base.BaseActivity
import io.github.kaczmarek.delishki.ui.base.BaseView
import kotlinx.android.synthetic.main.activity_list.*

interface TaskListView : BaseView

class TaskListActivity : BaseActivity(R.layout.activity_list), TaskListView,
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        val showTaskType = intent.getStringExtra(KEY_TYPE_TASKS) ?: Task.TODAY
        showContent(showTaskType)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showContent(showTaskType: String) {
        when (showTaskType) {
            Task.ANYTIME -> {
                title = getString(R.string.common_anytime)
                iv_list_image_type.setImageResource(R.drawable.ic_vector_anytime_purple)
            }
            Task.SOMEDAY -> {
                title = getString(R.string.common_someday)
                iv_list_image_type.setImageResource(R.drawable.ic_vector_someday_purple)
            }
            else -> {
                title = getString(R.string.common_today)
                iv_list_image_type.setImageResource(R.drawable.ic_vector_today_purple)
            }
        }
    }

    override fun onClick(v: View?) {

    }

    companion object {
        const val KEY_TYPE_TASKS = "KEY_TYPE_TASKS"
    }
}
