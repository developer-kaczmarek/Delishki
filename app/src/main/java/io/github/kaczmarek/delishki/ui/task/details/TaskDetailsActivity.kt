package io.github.kaczmarek.delishki.ui.task.details

import android.os.Bundle
import android.view.View
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.presentation.task.details.TaskDetailsPresenter
import io.github.kaczmarek.delishki.ui.base.BaseActivity
import io.github.kaczmarek.delishki.ui.base.BaseView
import moxy.ktx.moxyPresenter

interface TaskDetailsView : BaseView

class TaskDetailsActivity : BaseActivity(R.layout.activity_details), TaskDetailsView,
    View.OnClickListener {
    private val presenter by moxyPresenter { TaskDetailsPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {

    }
}
