package io.github.kaczmarek.delishki.ui.base

import android.view.MenuItem
import androidx.annotation.LayoutRes
import io.github.kaczmarek.delishki.util.Utils.hideSoftKeyboard
import moxy.MvpAppCompatActivity
import moxy.MvpView

interface BaseView : MvpView

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) :
    MvpAppCompatActivity(contentLayoutId),
    BaseView {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                hideSoftKeyboard(this)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}