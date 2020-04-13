package io.github.kaczmarek.delishki.util.ui.calendar.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import io.github.kaczmarek.delishki.util.ui.calendar.CalendarView
import org.threeten.bp.YearMonth


internal class CalendarLayoutManager(private val calView: CalendarView) :
    LinearLayoutManager(calView.context, HORIZONTAL, false) {

    private val adapter: CalendarAdapter
        get() = calView.adapter as CalendarAdapter

    private val context: Context
        get() = calView.context


    fun smoothScrollToMonth(month: YearMonth) {
        val position = adapter.getAdapterPosition(month)
        if (position != RecyclerView.NO_POSITION) {
            startSmoothScroll(CalendarSmoothScroller(position))
        }
    }

    private inner class CalendarSmoothScroller(position: Int) :
        LinearSmoothScroller(context) {

        init {
            targetPosition = position
        }

        override fun getHorizontalSnapPreference(): Int = SNAP_TO_START

    }
}