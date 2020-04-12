package io.github.kaczmarek.delishki.ui.task.calendar


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import io.github.kaczmarek.delishki.R
import io.github.kaczmarek.delishki.presentation.task.calendar.TaskCalendarPresenter
import io.github.kaczmarek.delishki.ui.base.BaseActivity
import io.github.kaczmarek.delishki.ui.base.BaseView
import io.github.kaczmarek.delishki.util.ui.calendar.models.CalendarDay
import io.github.kaczmarek.delishki.util.ui.calendar.models.CalendarMonth
import io.github.kaczmarek.delishki.util.ui.calendar.models.DayOwner
import io.github.kaczmarek.delishki.util.ui.calendar.models.RuDayOfWeek
import io.github.kaczmarek.delishki.util.ui.calendar.ui.DayBinder
import io.github.kaczmarek.delishki.util.ui.calendar.ui.MonthHeaderFooterBinder
import io.github.kaczmarek.delishki.util.ui.calendar.ui.ViewContainer
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.example_3_calendar_day.view.*
import kotlinx.android.synthetic.main.example_3_calendar_header.view.*
import moxy.ktx.moxyPresenter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.util.*

interface TaskCalendarView : BaseView

class TaskCalendarActivity : BaseActivity(R.layout.activity_calendar), TaskCalendarView,
    View.OnClickListener {
    private val presenter by moxyPresenter { TaskCalendarPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.common_plan)

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        exThreeCalendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10))
        exThreeCalendar.scrollToMonth(currentMonth)

        if (savedInstanceState == null) {
            exThreeCalendar.post {
                // Show today's events initially.
                //selectDate(today)
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exThreeDayText
            val dotView = view.exThreeDotView

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        //selectDate(day.date)
                    }
                }
            }
        }
        exThreeCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val dotView = container.dotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    /*when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.example_3_white)
                            textView.setBackgroundResource(R.drawable.example_3_today_bg)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.example_3_blue)
                            textView.setBackgroundResource(R.drawable.example_3_selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_3_black)
                            textView.background = null
                            dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }*/
                } else {
                    textView.setTextColor(ContextCompat.getColor(textView.context, R.color.colorPrimaryDark))
                    //dotView.makeInVisible()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = view.legendLayout
        }
        exThreeCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                }
            }
        }
    }

    override fun onClick(v: View?) {

    }

    fun daysOfWeekFromLocale(): Array<RuDayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = RuDayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }
}
