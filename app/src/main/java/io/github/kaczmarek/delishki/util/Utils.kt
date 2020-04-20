package io.github.kaczmarek.delishki.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes

object Utils {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    /**
     * Returns a localized formatted string
     */
    @JvmStatic
    fun getString(@StringRes id: Int, vararg parameters: Any): String {
        return context?.getString(id, *parameters)
            ?: throw IllegalStateException("Context in Utils not initialized. Please call UbUtils.init in your Application instance")
    }

    /**
     * Returns a Resources instance for the application's package
     */
    @JvmStatic
    fun getResources(): Resources {
        return context?.resources
            ?: throw IllegalStateException("Context in Utils not initialized. Please call UbUtils.init in your Application instance")
    }

    /**
     * Hide the keyboard
     */
    fun hideSoftKeyboard(context: Context) {
        try {
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                (context as Activity).currentFocus!!.windowToken,
                0
            )
            context.currentFocus!!.clearFocus()
        } catch (e: NullPointerException) {
            logError("KeyBoard", "NULL point exception in input method service")
        }

    }

    /**
     * Show the keyboard
     */
    fun openSoftKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Extension-функция для контекста, чтобы удобно преобразовывать dp в пиксели
     */
    fun dpToPx(dp: Int): Float = context?.let {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            it.resources.displayMetrics
        )
    } ?: throw IllegalStateException("$context must not be null")

    /**
     * Extension-функция для контекста, чтобы удобно преобразовывать dp в пиксели
     */
    fun pxToDp(px: Float): Float = context?.let {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, it.resources.displayMetrics)
    } ?: throw IllegalStateException("$context must not be null")


    /**
     * Extension-функция для контекста, чтобы удобно преобразовывать пиксели в sp
     */
    fun pxToSp(px: Float): Float = context?.let {
        px / it.resources.displayMetrics.scaledDensity
    } ?: throw IllegalStateException("$context must not be null")
}