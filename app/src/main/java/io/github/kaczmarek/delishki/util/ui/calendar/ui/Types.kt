package io.github.kaczmarek.delishki.util.ui.calendar.ui

import android.view.View
import io.github.kaczmarek.delishki.util.ui.calendar.models.CalendarDay
import io.github.kaczmarek.delishki.util.ui.calendar.models.CalendarMonth

open class ViewContainer(val view: View)

interface DayBinder<T : ViewContainer> {
    fun create(view: View): T
    fun bind(container: T, day: CalendarDay)
}

interface MonthHeaderFooterBinder<T : ViewContainer> {
    fun create(view: View): T
    fun bind(container: T, month: CalendarMonth)
}

typealias MonthScrollListener = (CalendarMonth) -> Unit