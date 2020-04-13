package io.github.kaczmarek.delishki.util.ui.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.UNSPECIFIED
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import io.github.kaczmarek.delishki.util.ui.calendar.models.*
import io.github.kaczmarek.delishki.util.ui.calendar.ui.*
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


    private val pagerSnapHelper = CalenderPageSnapHelper()

    private var startMonth: YearMonth? = null
    private var endMonth: YearMonth? = null

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
        val currentMonth = YearMonth.now()
        startMonth = currentMonth
        endMonth = currentMonth.plusMonths(10)
        addOnScrollListener(scrollListenerInternal)
        layoutManager = CalendarLayoutManager(this)
        adapter = CalendarAdapter(
            this,
            MonthConfig(currentMonth, currentMonth.plusMonths(10))
        )
        calendarLayoutManager.smoothScrollToMonth(currentMonth)
        pagerSnapHelper.attachToRecyclerView(this)
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
        if (adapter == null || layoutManager == null) return
        val state = layoutManager?.onSaveInstanceState()
        adapter = adapter
        layoutManager?.onRestoreInstanceState(state)
        post { calendarAdapter.notifyMonthScrollListenerIfNeeded() }
    }


    private fun updateAdapterViewConfig() {
        if (adapter != null) {
            invalidateViewHolders()
        }
    }

    fun notifyDayChanged(day: CalendarDay) {
        calendarAdapter.reloadDay(day)
    }


    fun notifyMonthChanged(month: YearMonth) {
        calendarAdapter.reloadMonth(month)
    }

    fun notifyCalendarChanged() {
        calendarAdapter.notifyDataSetChanged()
    }

    fun findFirstVisibleMonth(): CalendarMonth? {
        return calendarAdapter.findFirstVisibleMonth()
    }

    fun findLastVisibleMonth(): CalendarMonth? {
        return calendarAdapter.findLastVisibleMonth()
    }

    fun findFirstVisibleDay(): CalendarDay? {
        return calendarAdapter.findFirstVisibleDay()
    }

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

    companion object {
        const val DAY_SIZE_SQUARE = Int.MIN_VALUE
    }
}