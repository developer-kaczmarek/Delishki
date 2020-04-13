package io.github.kaczmarek.delishki.util.ui.calendar.ui

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.util.ui.calendar.CalendarView
import io.github.kaczmarek.delishki.util.ui.calendar.models.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

internal typealias LP = ViewGroup.LayoutParams


internal class CalendarAdapter(
    private val calView: CalendarView,
    internal var monthConfig: MonthConfig
) : RecyclerView.Adapter<MonthViewHolder>() {

    private val months: List<CalendarMonth>
        get() = monthConfig.months

    val bodyViewId = ViewCompat.generateViewId()
    val rootViewId = ViewCompat.generateViewId()
    var headerViewId = ViewCompat.generateViewId()

    init {
        setHasStableIds(true)
    }

    private val isAttached: Boolean
        get() = calView.adapter === this

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        calView.post { notifyMonthScrollListenerIfNeeded() }
    }

    private fun getItem(position: Int): CalendarMonth = months[position]

    override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()

    override fun getItemCount(): Int = months.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val context = parent.context
        val rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            id = rootViewId
        }

        val monthHeaderView = LayoutInflater.from(context).inflate(R.layout.component_calendarview_rv_header, rootLayout, false)
        if (monthHeaderView.id == View.NO_ID) {
            monthHeaderView.id = headerViewId
        } else {
            headerViewId = monthHeaderView.id
        }
        rootLayout.addView(monthHeaderView)

        val monthBodyLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LP.WRAP_CONTENT, LP.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            id = bodyViewId
        }
        rootLayout.addView(monthBodyLayout)

        fun setupRoot(root: ViewGroup) {
            ViewCompat.setPaddingRelative(
                root,
                calView.monthPaddingStart, calView.monthPaddingTop,
                calView.monthPaddingEnd, calView.monthPaddingBottom
            )
            root.layoutParams = ViewGroup.MarginLayoutParams(LP.WRAP_CONTENT, LP.WRAP_CONTENT).apply {
                bottomMargin = calView.monthMarginBottom
                topMargin = calView.monthMarginTop
            }
        }

        val userRoot = rootLayout.apply { setupRoot(this) }

        @Suppress("UNCHECKED_CAST")
        return MonthViewHolder(
            this, userRoot,
            DayConfig(
                calView.dayWidth, calView.dayHeight,
                calView.dayBinder as DayBinder<ViewContainer>
            ),
            calView.monthHeaderBinder as MonthHeaderFooterBinder<ViewContainer>?
        )
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                holder.reloadDay(it as CalendarDay)
            }
        }
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bindMonth(getItem(position))
    }

    fun reloadDay(day: CalendarDay) {
        val position = getAdapterPosition(day)
        if (position != RecyclerView.NO_POSITION) {
            notifyItemChanged(position, day)
        }
    }

    fun reloadMonth(month: YearMonth) {
        notifyItemChanged(getAdapterPosition(month))
    }

    private var visibleMonth: CalendarMonth? = null
    private var calWrapsHeight: Boolean? = null
    private var initialLayout = true
    fun notifyMonthScrollListenerIfNeeded() {
        if (!isAttached) return

        if (calView.isAnimating) {
            calView.itemAnimator?.isRunning {
                notifyMonthScrollListenerIfNeeded()
            }
            return
        }
        val visibleItemPos = findFirstVisibleMonthPosition()
        if (visibleItemPos != RecyclerView.NO_POSITION) {
            val visibleMonth = months[visibleItemPos]

            if (visibleMonth != this.visibleMonth) {
                this.visibleMonth = visibleMonth
                calView.monthScrollListener?.invoke(visibleMonth)
                val calWrapsHeight = calWrapsHeight ?: (calView.layoutParams.height == LP.WRAP_CONTENT).also {
                    calWrapsHeight = it
                }
                if (!calWrapsHeight) return // Bug only happens when the CalenderView wraps its height.
                val visibleVH =
                    calView.findViewHolderForAdapterPosition(visibleItemPos) as? MonthViewHolder ?: return
                val newHeight = (visibleVH.headerView?.height?: 0)+
                    visibleMonth.weekDays.size * calView.dayHeight
                if (calView.height != newHeight) {
                    ValueAnimator.ofInt(calView.height, newHeight).apply {
                        duration = if (initialLayout) 0 else 500
                        addUpdateListener {
                            calView.updateLayoutParams { height = it.animatedValue as Int }
                            visibleVH.itemView.requestLayout()
                        }
                        start()
                    }
                }
                if (initialLayout) initialLayout = false
            }
        }
    }

    internal fun getAdapterPosition(month: YearMonth): Int {
        return months.indexOfFirst { it.yearMonth == month }
    }

    internal fun getAdapterPosition(date: LocalDate): Int {
        return getAdapterPosition(CalendarDay(date, DayOwner.THIS_MONTH))
    }

    private fun getAdapterPosition(day: CalendarDay): Int {
        val firstMonthIndex = getAdapterPosition(day.positionYearMonth)
        if (firstMonthIndex == RecyclerView.NO_POSITION) return RecyclerView.NO_POSITION

        val firstCalMonth = months[firstMonthIndex]
        val sameMonths = months.slice(firstMonthIndex until firstMonthIndex + firstCalMonth.numberOfSameMonth)
        val indexWithDateInSameMonth = sameMonths.indexOfFirst { months ->
            months.weekDays.any { weeks -> weeks.any { it == day } }
        }
        return if (indexWithDateInSameMonth == RecyclerView.NO_POSITION) RecyclerView.NO_POSITION else firstMonthIndex + indexWithDateInSameMonth
    }

    private val layoutManager: CalendarLayoutManager
        get() = calView.layoutManager as CalendarLayoutManager

    fun findFirstVisibleMonth(): CalendarMonth? = months.getOrNull(findFirstVisibleMonthPosition())

    fun findLastVisibleMonth(): CalendarMonth? = months.getOrNull(findLastVisibleMonthPosition())

    fun findFirstVisibleDay(): CalendarDay? = findVisibleDay(true)

    fun findLastVisibleDay(): CalendarDay? = findVisibleDay(false)

    private fun findFirstVisibleMonthPosition(): Int = findVisibleMonthPosition(true)

    private fun findLastVisibleMonthPosition(): Int = findVisibleMonthPosition(false)

    private fun findVisibleMonthPosition(isFirst: Boolean): Int {
        val visibleItemPos =
            if (isFirst) layoutManager.findFirstVisibleItemPosition() else layoutManager.findLastVisibleItemPosition()

        if (visibleItemPos != RecyclerView.NO_POSITION) {

            // We make sure that the view for the returned position is visible to a reasonable degree.
            val visibleItemPx = Rect().let { rect ->
                val visibleItemView = layoutManager.findViewByPosition(visibleItemPos) ?: return RecyclerView.NO_POSITION
                visibleItemView.getGlobalVisibleRect(rect)
                return@let rect.right - rect.left
            }

            // Fixes an issue where using DAY_SIZE_SQUARE with a paged calendar causes
            // some dates to stretch slightly outside the intended bounds due to pixel
            // rounding. Hence finding the first visible index will return the view
            // with the px outside bounds. 7 is the number of cells in a week.
            if (visibleItemPx <= 7) {
                val nextItemPosition = if (isFirst) visibleItemPos + 1 else visibleItemPos - 1
                return if (months.indices.contains(nextItemPosition)) {
                    nextItemPosition
                } else {
                    visibleItemPos
                }
            }

        }

        return visibleItemPos
    }

    private fun findVisibleDay(isFirst: Boolean): CalendarDay? {
        val visibleIndex = if (isFirst) findFirstVisibleMonthPosition() else findLastVisibleMonthPosition()
        if (visibleIndex == RecyclerView.NO_POSITION) return null

        val visibleItemView = layoutManager.findViewByPosition(visibleIndex) ?: return null
        val monthRect = Rect()
        visibleItemView.getGlobalVisibleRect(monthRect)

        val dayRect = Rect()
        return months[visibleIndex].weekDays.flatten()
            .run { if (isFirst) this else reversed() }
            .firstOrNull {
                val dayView = visibleItemView.findViewById<View?>(it.date.hashCode()) ?: return@firstOrNull false
                dayView.getGlobalVisibleRect(dayRect)
                dayRect.intersect(monthRect)
            }
    }
}