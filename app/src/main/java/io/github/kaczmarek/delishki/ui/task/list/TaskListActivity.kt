package io.github.kaczmarek.delishki.ui.task.list


import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.domain.task.entity.Task
import io.github.kaczmarek.delishki.presentation.task.list.TaskListPresenter
import io.github.kaczmarek.delishki.ui.base.BaseActivity
import io.github.kaczmarek.delishki.ui.base.BaseView
import io.github.kaczmarek.delishki.util.visible
import kotlinx.android.synthetic.main.activity_list.*
import moxy.ktx.moxyPresenter

interface TaskListView : BaseView {
    fun showCountTasks(count: Int)
}

class TaskListActivity : BaseActivity(R.layout.activity_list), TaskListView,
    View.OnClickListener {

    private val presenter by moxyPresenter { TaskListPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val showTaskType = intent.getStringExtra(KEY_TYPE_TASKS) ?: Task.TODAY
        showContent(showTaskType)
        presenter.getCountTasks(showTaskType)
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
            Task.PLANS -> {
                title = getString(R.string.common_plan)
                iv_list_image_type.setImageResource(R.drawable.ic_vector_plan_purple)
            }
            else -> {
                title = getString(R.string.common_today)
                iv_list_image_type.setImageResource(R.drawable.ic_vector_today_purple)
            }
        }
    }


    override fun onClick(v: View?) {

    }

    override fun showCountTasks(count: Int) {
        tv_list_count_tasks.text = resources.getQuantityString(
            R.plurals.common_tasks,
            count,
            count
        )
        if (count == 0) {
            showEmptyPlaceholder()
        }
    }


    private fun showEmptyPlaceholder() {
        cl_list_empty_placeholder.visible
        fab_list_empty_placeholder
            .animate()
            .setStartDelay(500)
            .scaleY(1f)
            .scaleX(1f)
            .translationY(0f)
            .setInterpolator(DecelerateInterpolator(2f))
            .start()
    }

    companion object {
        const val KEY_TYPE_TASKS = "KEY_TYPE_TASKS"
    }
}
