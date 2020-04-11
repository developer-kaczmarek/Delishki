package io.github.kaczmarek.delishki.util

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import io.github.kaczmarek.delishki.R

/**
 * Created by Umalt on 30.03.2020
 */

@Suppress("DEPRECATION")
fun Drawable.changeColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = when {
        isVisible -> View.VISIBLE
        else -> View.GONE
    }
}

/**
 * Display Snackbar
 * @param msgResId - строковый ресурс для сообщения
 * @param actionMsgResId - строковый ресурс для текста кнопки
 * @param duration - время отображения
 * @param action - функция, выполняющаяся по клику на кнопку
 * @param callback - слушатель, предназначенный для отслеживания скрытия Snackbar'а
 */
fun View.snackbar(
    @StringRes msgResId: Int?,
    @StringRes actionMsgResId: Int? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: () -> Unit = {},
    callback: Snackbar.Callback? = null
) {
    snackbar(
        msgResId?.let { Utils.getString(it) },
        actionMsgResId?.let { Utils.getString(it) },
        duration,
        action,
        callback
    )
}

fun View.snackbar(
    message: String?,
    actionMsg: String? = null,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: () -> Unit = {},
    callback: Snackbar.Callback? = null
) {
    message?.let { msg ->
        val snackbar = Snackbar.make(this, msg, duration)

        actionMsg?.let {
            snackbar.apply {
                setActionTextColor(Color.MAGENTA)
                setAction(actionMsg) { action() }
            }
        }

        callback?.let { snackbar.addCallback(it) }

        snackbar.show()
    }
}