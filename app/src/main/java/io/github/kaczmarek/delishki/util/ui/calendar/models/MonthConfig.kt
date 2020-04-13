package io.github.kaczmarek.delishki.util.ui.calendar.models


import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields

data class MonthConfig(
    val startMonth: YearMonth,
    val endMonth: YearMonth
) {

    internal val months: List<CalendarMonth> by lazy lazy@{
        generateBoundedMonths(
            startMonth, endMonth
        )
    }

    internal companion object {
        internal fun generateBoundedMonths(
            startMonth: YearMonth,
            endMonth: YearMonth
        ): List<CalendarMonth> {
            val months = mutableListOf<CalendarMonth>()
            var currentMonth = startMonth
            while (currentMonth <= endMonth) {
                val weekDaysGroup =
                    generateWeekDays(currentMonth)

                // Group rows by maxRowCount into CalendarMonth classes.
                val calendarMonths = mutableListOf<CalendarMonth>()
                val numberOfSameMonth = weekDaysGroup.size roundDiv 6
                var indexInSameMonth = 0
                calendarMonths.addAll(weekDaysGroup.chunked(6) { monthDays ->
                    // Use monthDays.toList() to create a copy of the ephemeral list.
                    CalendarMonth(
                        currentMonth,
                        monthDays.toList(),
                        indexInSameMonth++,
                        numberOfSameMonth
                    )
                })

                months.addAll(calendarMonths)
                if (currentMonth != endMonth) currentMonth = YearMonth.of(currentMonth.year, currentMonth.month).plusMonths(1) else break
            }

            return months
        }

        /**
         * Generates the necessary number of weeks for a [YearMonth].
         */
        private fun generateWeekDays(
            yearMonth: YearMonth
        ): List<List<CalendarDay>> {
            val year = yearMonth.year
            val month = yearMonth.monthValue

            val thisMonthDays = (1..yearMonth.lengthOfMonth()).map {
                CalendarDay(LocalDate.of(year, month, it), DayOwner.THIS_MONTH)
            }

            val weekDaysGroup = run {
                // Group days by week of month so we can add the in dates if necessary.
                val weekOfMonthField = WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth()
                val groupByWeekOfMonth =
                    thisMonthDays.groupBy { it.date.get(weekOfMonthField) }.values.toMutableList()

                // Add in-dates if necessary
                val firstWeek = groupByWeekOfMonth.first()
                if (firstWeek.size < 7) {
                    val previousMonth = yearMonth.minusMonths(1)
                    val inDates = (1..previousMonth.lengthOfMonth()).toList()
                        .takeLast(7 - firstWeek.size).map {
                            CalendarDay(
                                LocalDate.of(previousMonth.year, previousMonth.month, it),
                                DayOwner.PREVIOUS_MONTH
                            )
                        }
                    groupByWeekOfMonth[0] = inDates + firstWeek
                }
                groupByWeekOfMonth
            }


            if (weekDaysGroup.last().size < 7) {
                val lastWeek = weekDaysGroup.last()
                val lastDay = lastWeek.last()
                val outDates = (1..7 - lastWeek.size).map {
                    CalendarDay(lastDay.date.plusDays(it.toLong()), DayOwner.NEXT_MONTH)
                }
                weekDaysGroup[weekDaysGroup.lastIndex] = lastWeek + outDates
            }

            return weekDaysGroup
        }
    }
}

private infix fun Int.roundDiv(other: Int): Int {
    val div = this / other
    val rem = this % other
    return if (rem == 0) div else div + 1
}