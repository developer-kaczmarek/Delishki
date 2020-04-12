package io.github.kaczmarek.delishki.util.ui.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.util.ui.calendar.models.*
import io.github.kaczmarek.delishki.util.ui.calendar.ui.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

open class CalendarView : RecyclerView {

    /**
     * The [DayBinder] instance used for managing day cell views
     * creation and reuse. Changing the binder means that the view
     * creation logic could have changed too. We refresh the Calender.
     */
    var dayBinder: DayBinder<*>? = null
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The [MonthHeaderFooterBinder] instance used for managing header views.
     * The header view is shown above each month on the Calendar.
     */
    var monthHeaderBinder: MonthHeaderFooterBinder<*>? = null
        set(value) {
            field = value
            invalidateViewHolders()
        }

    var monthScrollListener: MonthScrollListener? = null

    /**
     * The xml resource that is inflated and used as the day cell view.
     * This must be provided.
     */
    var dayViewResource = 0
        set(value) {
            if (field != value) {
                if (value == 0) throw IllegalArgumentException("'dayViewResource' attribute not provided.")
                field = value
                updateAdapterViewConfig()
            }
        }

    /**
     * The xml resource that is inflated and used as a header for every month.
     * Set zero to disable.
     */
    var monthHeaderResource = 0
        set(value) {
            if (field != value) {
                field = value
                updateAdapterViewConfig()
            }
        }

    var monthViewClass: String? = null


    /**
     * The [RecyclerView.Orientation] used for the layout manager.
     * This determines the scroll direction of the the calendar.
     */
    @Orientation
    var orientation = HORIZONTAL

    private val pagerSnapHelper = CalenderPageSnapHelper()

    private var startMonth: YearMonth? = null
    private var endMonth: YearMonth? = null
    private var firstDayOfWeek = DayOfWeek.MONDAY

    private var autoSize = true
    private var sizedInternally = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr, defStyleAttr)
    }

    private fun init(attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        if (isInEditMode) return
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.CalendarView, defStyleAttr, defStyleRes)
        dayViewResource = a.getResourceId(R.styleable.CalendarView_cv_dayViewResource, dayViewResource)
        monthHeaderResource = R.layout.example_3_calendar_header
        pagerSnapHelper.attachToRecyclerView(this)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (autoSize && isInEditMode.not()) {
            val widthMode = MeasureSpec.getMode(widthMeasureSpec)
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)

            if (widthMode == UNSPECIFIED && heightMode == UNSPECIFIED) {
                throw UnsupportedOperationException("Cannot calculate the values for day Width/Height with the current configuration.")
            }

            // +0.5 => round to the nearest pixel
            val squareSize = (((widthSize - (monthPaddingStart + monthPaddingEnd)) / 7f) + 0.5).toInt()
            if (dayWidth != squareSize || dayHeight != squareSize) {
                sizedInternally = true
                dayWidth = squareSize
                dayHeight = squareSize
                sizedInternally = false
                invalidateViewHolders()
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    /**
     * The width, in pixels for each day cell view.
     * Set this to [DAY_SIZE_SQUARE] to have a nice
     * square item view.
     *
     * @see [DAY_SIZE_SQUARE]
     */
    @Px
    var dayWidth: Int = DAY_SIZE_SQUARE
        set(value) {
            field = value
            if (sizedInternally.not()) {
                autoSize = value == DAY_SIZE_SQUARE
                invalidateViewHolders()
            }
        }

    /**
     * The height, in pixels for each day cell view.
     * Set this to [DAY_SIZE_SQUARE] to have a nice
     * square item view.
     *
     * @see [DAY_SIZE_SQUARE]
     */
    @Px
    var dayHeight: Int = DAY_SIZE_SQUARE
        set(value) {
            field = value
            if (sizedInternally.not()) {
                autoSize = value == DAY_SIZE_SQUARE
                invalidateViewHolders()
            }
        }

    /**
     * The padding, in pixels to be applied
     * to the start of each month view.
     */
    @Px
    var monthPaddingStart = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The padding, in pixels to be applied
     * to the end of each month view.
     */
    @Px
    var monthPaddingEnd = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The padding, in pixels to be applied
     * to the top of each month view.
     */
    @Px
    var monthPaddingTop = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The padding, in pixels to be applied
     * to the bottom of each month view.
     */
    @Px
    var monthPaddingBottom = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The margin, in pixels to be applied
     * to the start of each month view.
     */
    @Px
    var monthMarginStart = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The margin, in pixels to be applied
     * to the end of each month view.
     */
    @Px
    var monthMarginEnd = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The margin, in pixels to be applied
     * to the top of each month view.
     */
    @Px
    var monthMarginTop = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    /**
     * The margin, in pixels to be applied
     * to the bottom of each month view.
     */
    @Px
    var monthMarginBottom = 0
        set(value) {
            field = value
            invalidateViewHolders()
        }

    private val calendarLayoutManager: CalendarLayoutManager
        get() = layoutManager as CalendarLayoutManager

    private val calendarAdapter: CalendarAdapter
        get() = adapter as CalendarAdapter

    private fun invalidateViewHolders() {
        // This does not remove visible views.
        // recycledViewPool.clear()

        // This removes all views but is internal.
        // removeAndRecycleViews()

        if (adapter == null || layoutManager == null) return
        val state = layoutManager?.onSaveInstanceState()
        adapter = adapter
        layoutManager?.onRestoreInstanceState(state)
        post { calendarAdapter.notifyMonthScrollListenerIfNeeded() }
    }

    private fun updateAdapterMonthConfig() {
        if (adapter != null) {
            calendarAdapter.monthConfig =
                MonthConfig(
                    startMonth ?: return,
                    endMonth ?: return,
                    firstDayOfWeek
                )
            calendarAdapter.notifyDataSetChanged()
            post { calendarAdapter.notifyMonthScrollListenerIfNeeded() }
        }
    }

    private fun updateAdapterViewConfig() {
        if (adapter != null) {
            calendarAdapter.viewConfig =
                ViewConfig(dayViewResource, monthHeaderResource, monthViewClass)
            invalidateViewHolders()
        }
    }

    /**
     * Scroll to a specific month on the calendar. This only
     * shows the view for the month without any animations.
     * For a smooth scrolling effect, use [smoothScrollToMonth]
     */
    fun scrollToMonth(month: YearMonth) {
        calendarLayoutManager.scrollToMonth(month)
    }

    /**
     * Scroll to a specific month on the calendar using a smooth scrolling animation.
     * Just like [scrollToMonth], but with a smooth scrolling animation.
     */
    fun smoothScrollToMonth(month: YearMonth) {
        calendarLayoutManager.smoothScrollToMonth(month)
    }

    /**
     * Scroll to a specific [CalendarDay]. This brings the date cell
     * view's top to the top of the CalendarVew in vertical mode or
     * the cell view's left edge to the left edge of the CalendarVew
     * in horizontal mode. No animation is performed.
     * For a smooth scrolling effect, use [smoothScrollToDay].
     */
    fun scrollToDay(day: CalendarDay) {
        calendarLayoutManager.scrollToDay(day)
    }

    /**
     * Shortcut for [scrollToDay] with a [LocalDate] instance.
     */
    @JvmOverloads
    fun scrollToDate(date: LocalDate, owner: DayOwner = DayOwner.THIS_MONTH) {
        scrollToDay(CalendarDay(date, owner))
    }

    /**
     * Scroll to a specific [CalendarDay] using a smooth scrolling animation.
     * Just like [scrollToDay], but with a smooth scrolling animation.
     */
    fun smoothScrollToDay(day: CalendarDay) {
        calendarLayoutManager.smoothScrollToDay(day)
    }

    /**
     * Shortcut for [smoothScrollToDay] with a [LocalDate] instance.
     */
    @JvmOverloads
    fun smoothScrollToDate(date: LocalDate, owner: DayOwner = DayOwner.THIS_MONTH) {
        smoothScrollToDay(CalendarDay(date, owner))
    }

    /**
     * Notify the CalendarView to reload the cell for this [CalendarDay]
     * This causes [DayBinder.bind] to be called with the [ViewContainer]
     * at this position. Use this to reload a date cell on the Calendar.
     */
    fun notifyDayChanged(day: CalendarDay) {
        calendarAdapter.reloadDay(day)
    }

    /**
     * Shortcut for [notifyDayChanged] with a [LocalDate] instance.
     */
    @JvmOverloads
    fun notifyDateChanged(date: LocalDate, owner: DayOwner = DayOwner.THIS_MONTH) {
        notifyDayChanged(CalendarDay(date, owner))
    }

    /**
     * Notify the CalendarView to reload the view for this [YearMonth]
     * This causes the following sequence pf events:
     * [DayBinder.bind] will be called for all dates in this month.
     * [MonthHeaderFooterBinder.bind] will be called for this month's header view if available.
     * [MonthHeaderFooterBinder.bind] will be called for this month's footer view if available.
     */
    fun notifyMonthChanged(month: YearMonth) {
        calendarAdapter.reloadMonth(month)
    }

    /**
     * Notify the CalendarView to reload all months.
     * Essentially calls [RecyclerView.Adapter.notifyDataSetChanged] on the adapter.
     */
    fun notifyCalendarChanged() {
        calendarAdapter.notifyDataSetChanged()
    }

    /**
     * Find the first visible month on the CalendarView.
     *
     * @return The first visible month or null if not found.
     */
    fun findFirstVisibleMonth(): CalendarMonth? {
        return calendarAdapter.findFirstVisibleMonth()
    }

    /**
     * Find the last visible month on the CalendarView.
     *
     * @return The last visible month or null if not found.
     */
    fun findLastVisibleMonth(): CalendarMonth? {
        return calendarAdapter.findLastVisibleMonth()
    }

    /**
     * Find the first visible day on the CalendarView.
     * This is the day at the top-left corner of the calendar.
     *
     * @return The first visible day or null if not found.
     */
    fun findFirstVisibleDay(): CalendarDay? {
        return calendarAdapter.findFirstVisibleDay()
    }

    /**
     * Find the last visible day on the CalendarView.
     * This is the day at the bottom-right corner of the calendar.
     *
     * @return The last visible day or null if not found.
     */
    fun findLastVisibleDay(): CalendarDay? {
        return calendarAdapter.findLastVisibleDay()
    }

    private val scrollListenerInternal = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == SCROLL_STATE_IDLE) {
                calendarAdapter.notifyMonthScrollListenerIfNeeded()
            }
        }
    }

    /**
     * Setup the CalendarView. You can call this any time to change the
     * the desired [startMonth], [endMonth] or [firstDayOfWeek] on the Calendar.
     * See [updateStartMonth], [updateEndMonth] and [updateMonthRange] for more refined updates.
     *
     * @param startMonth The first month on the calendar.
     * @param endMonth The last month on the calendar.
     * @param firstDayOfWeek An instance of [DayOfWeek] enum to be the first day of week.
     */
    fun setup(startMonth: YearMonth, endMonth: YearMonth) {
        if (this.startMonth != null && this.endMonth != null) {
            this.firstDayOfWeek = DayOfWeek.MONDAY
            updateMonthRange(startMonth, endMonth)
        } else {
            this.startMonth = startMonth
            this.endMonth = endMonth
            this.firstDayOfWeek = DayOfWeek.MONDAY

            // Remove the listener before adding again to prevent
            // multiple additions if we already added it before.
            removeOnScrollListener(scrollListenerInternal)
            addOnScrollListener(scrollListenerInternal)

            layoutManager = CalendarLayoutManager(this, orientation)
            adapter = CalendarAdapter(
                this,
                ViewConfig(dayViewResource, monthHeaderResource, monthViewClass),
                MonthConfig(startMonth,
                    endMonth, firstDayOfWeek
                )
            )
        }
    }

    /**
     * Update the CalendarView's start month.
     * This can be called only if you have called [setup] in the past.
     * See [updateEndMonth] and [updateMonthRange].
     */
    fun updateStartMonth(startMonth: YearMonth) = updateMonthRange(
        startMonth,
        endMonth ?: throw IllegalStateException("`endMonth` is not set. Have you called `setup()`?")
    )

    /**
     * Update the CalendarView's end month.
     * This can be called only if you have called [setup] in the past.
     * See [updateStartMonth] and [updateMonthRange].
     */
    fun updateEndMonth(endMonth: YearMonth) = updateMonthRange(
        startMonth ?: throw IllegalStateException("`startMonth` is not set. Have you called `setup()`?"),
        endMonth
    )

    /**
     * Update the CalendarView's start and end months.
     * This can be called only if you have called [setup] in the past.
     * See [updateStartMonth] and [updateEndMonth].
     */
    fun updateMonthRange(startMonth: YearMonth, endMonth: YearMonth) {
        this.startMonth = startMonth
        this.endMonth = endMonth

        val oldConfig = calendarAdapter.monthConfig
        val newConfig = MonthConfig(
            startMonth,
            endMonth,
            firstDayOfWeek
        )
        calendarAdapter.monthConfig = newConfig
        DiffUtil.calculateDiff(MonthRangeDiffCallback(oldConfig.months, newConfig.months), false)
            .dispatchUpdatesTo(calendarAdapter)
    }

    private class MonthRangeDiffCallback(
        private val oldItems: List<CalendarMonth>,
        private val newItems: List<CalendarMonth>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

    companion object {
        /**
         * A value for [dayWidth] and [dayHeight] which indicates that the day
         * cells should have equal width and height. Each view's width and height
         * will be the width of the calender divided by 7.
         */
        const val DAY_SIZE_SQUARE = Int.MIN_VALUE
    }
}