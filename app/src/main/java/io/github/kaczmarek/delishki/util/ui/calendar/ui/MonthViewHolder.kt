package io.github.kaczmarek.delishki.util.ui.calendar.ui

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import io.github.kaczmarek.delishki.util.ui.calendar.models.CalendarDay
import io.github.kaczmarek.delishki.util.ui.calendar.models.CalendarMonth

internal class MonthViewHolder constructor(
    adapter: CalendarAdapter,
    rootLayout: ViewGroup,
    dayConfig: DayConfig,
    private var monthHeaderBinder: MonthHeaderFooterBinder<ViewContainer>?
) : RecyclerView.ViewHolder(rootLayout) {

    private val weekHolders = (1..6).map { WeekHolder(dayConfig) }

    val headerView: View? = rootLayout.findViewById(adapter.headerViewId)
    val bodyLayout: LinearLayout = rootLayout.findViewById(adapter.bodyViewId)

    private var headerContainer: ViewContainer? = null
    private var footerContainer: ViewContainer? = null

    lateinit var month: CalendarMonth

    init {
        // Add week rows.
        weekHolders.forEach {
            bodyLayout.addView(it.inflateWeekView(bodyLayout))
        }
    }

    fun bindMonth(month: CalendarMonth) {
        this.month = month
        headerView?.let { view ->
            val headerContainer = headerContainer ?: monthHeaderBinder!!.create(view).also {
                headerContainer = it
            }
            monthHeaderBinder?.bind(headerContainer, month)
        }
        weekHolders.forEachIndexed { index, week ->
            week.bindWeekView(month.weekDays.getOrNull(index).orEmpty())
        }
    }

    fun reloadDay(day: CalendarDay) {
        weekHolders.map { it.dayHolders }.flatten().firstOrNull { it.day == day }?.reloadView()
    }

}