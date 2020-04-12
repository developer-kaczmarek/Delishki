package io.github.kaczmarek.delishki.util.ui.calendar.models

import io.github.kaczmarek.delishki.util.ui.calendar.utils.next
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields

data class MonthConfig(
    val startMonth: YearMonth,
    val endMonth: YearMonth,
    val firstDayOfWeek: DayOfWeek
) {

    internal val months: List<CalendarMonth> by lazy lazy@{
        generateBoundedMonths(
            startMonth, endMonth, firstDayOfWeek
        )
    }

    internal companion object {

        /**
         * A [YearMonth] will have multiple [CalendarMonth] instances if the [maxRowCount] is
         * less than 6. Each [CalendarMonth] will hold just enough [CalendarDay] instances(weekDays)
         * to fit in the [maxRowCount].
         */
        internal fun generateBoundedMonths(
            startMonth: YearMonth,
            endMonth: YearMonth,
            firstDayOfWeek: DayOfWeek
        ): List<CalendarMonth> {
            val months = mutableListOf<CalendarMonth>()
            var currentMonth = startMonth
            while (currentMonth <= endMonth) {
                val generateInDates = true

                val weekDaysGroup =
                    generateWeekDays(currentMonth, firstDayOfWeek, generateInDates)

                // Group rows by maxRowCount into CalendarMonth classes.
                val calendarMonths = mutableListOf<CalendarMonth>()
                val numberOfSameMonth = weekDaysGroup.size roundDiv 6
                var indexInSameMonth = 0
                calendarMonths.addAll(weekDaysGroup.chunked(6) { monthDays ->
                    // Use monthDays.toList() to create a copy of the ephemeral list.
                    CalendarMonth(currentMonth, monthDays.toList(), indexInSameMonth++, numberOfSameMonth)
                })

                months.addAll(calendarMonths)
                if (currentMonth != endMonth) currentMonth = currentMonth.next else break
            }

            return months
        }

        internal fun generateUnboundedMonths(
            startMonth: YearMonth,
            endMonth: YearMonth,
            firstDayOfWeek: DayOfWeek
        ): List<CalendarMonth> {

            // Generate a flat list of all days in the given month range
            val allDays = mutableListOf<CalendarDay>()
            var currentMonth = startMonth
            while (currentMonth <= endMonth) {

                // If inDates are enabled with boundaries disabled,
                // we show them on the first month only.
                val generateInDates = currentMonth == startMonth


                allDays.addAll(
                    // We don't generate outDates for any month, they are added manually down below.
                    // This is because if outDates are enabled with boundaries disabled, we show them
                    // on the last month only.
                    generateWeekDays(currentMonth, firstDayOfWeek, generateInDates).flatten()
                )
                if (currentMonth != endMonth) currentMonth = currentMonth.next else break
            }

            // Regroup data into 7 days. Use toList() to create a copy of the ephemeral list.
            val allDaysGroup = allDays.chunked(7).toList()

            val calendarMonths = mutableListOf<CalendarMonth>()
            val calMonthsCount = allDaysGroup.size roundDiv 6
            allDaysGroup.chunked(6) { ephemeralMonthWeeks ->
                val monthWeeks = ephemeralMonthWeeks.toMutableList()

                // Add the outDates for the last row if needed.
                if (monthWeeks.last().size < 7) {
                    val lastWeek = monthWeeks.last()
                    val lastDay = lastWeek.last()
                    val outDates = (1..7 - lastWeek.size).map {
                        CalendarDay(lastDay.date.plusDays(it.toLong()), DayOwner.NEXT_MONTH)
                    }
                    monthWeeks[monthWeeks.lastIndex] = lastWeek + outDates
                }

                calendarMonths.add(
                    // numberOfSameMonth is the total number of all months and
                    // indexInSameMonth is basically this item's index in the entire month list.
                    CalendarMonth(startMonth, monthWeeks, calendarMonths.size, calMonthsCount)
                )
            }

            return calendarMonths
        }

        /**
         * Generates the necessary number of weeks for a [YearMonth].
         */
        internal fun generateWeekDays(
            yearMonth: YearMonth,
            firstDayOfWeek: DayOfWeek,
            generateInDates: Boolean
        ): List<List<CalendarDay>> {
            val year = yearMonth.year
            val month = yearMonth.monthValue

            val thisMonthDays = (1..yearMonth.lengthOfMonth()).map {
                CalendarDay(LocalDate.of(year, month, it), DayOwner.THIS_MONTH)
            }

            val weekDaysGroup = if (generateInDates) {
                // Group days by week of month so we can add the in dates if necessary.
                val weekOfMonthField = WeekFields.of(firstDayOfWeek, 1).weekOfMonth()
                val groupByWeekOfMonth = thisMonthDays.groupBy { it.date.get(weekOfMonthField) }.values.toMutableList()

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
            } else {
                // Group days by 7, first day shown on the month will be day 1.
                // Use toMutableList() to create a copy of the ephemeral list.
                thisMonthDays.chunked(7).toMutableList()
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

/**
 * We want the remainder to be added as the division result.
 * E.g: 5/2 should be 3.
 */
private infix fun Int.roundDiv(other: Int): Int {
    val div = this / other
    val rem = this % other
    // Add the last value dropped from div if rem is not zero
    return if (rem == 0) div else div + 1
}