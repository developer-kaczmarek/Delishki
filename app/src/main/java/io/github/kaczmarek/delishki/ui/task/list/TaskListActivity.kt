package io.github.kaczmarek.delishki.ui.task.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.domain.task.entity.Task
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
        val showTaskType = intent.getStringExtra(KEY_TYPE_TASKS) ?: Task.TODAY
        showContent(showTaskType)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    companion object {
        const val KEY_TYPE_TASKS = "KEY_TYPE_TASKS"
    }
}
