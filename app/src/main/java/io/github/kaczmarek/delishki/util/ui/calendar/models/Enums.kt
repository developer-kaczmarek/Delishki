package io.github.kaczmarek.delishki.util.ui.calendar.models

import org.threeten.bp.DayOfWeek

enum class DayOwner {
    /**
     * Belongs to the previous month on the calendar.
     * Such days are referred to as outDates.
     */
    PREVIOUS_MONTH,

    /**
     * Belongs to the current month on the calendar.
     * Such days are referred to as monthDates.
     */
    THIS_MONTH,

    /**
     * Belongs to the next month on the calendar.
     * Such days are referred to as outDates.
     */
    NEXT_MONTH
}

/**
 * Determines how outDates are generated for each month on the calendar.
 */
/*enum class OutDateStyle {
    *//**
     * The calendar will generate outDates until it reaches
     * the first end of a row. This means that if  a month
     * has 6 rows, it will display 6 rows and if a month
     * has 5 rows, it will display 5 rows.
     *//*
    END_OF_ROW,

    *//**
     * The calendar will generate outDates until
     * it reaches the end of a 6 x 7 grid.
     * This means that all months will have 6 rows.
     *//*
    END_OF_GRID,

    *//**
     * outDates will not be generated.
     *//*
    NONE
}*/

enum class RuDayOfWeek {
    ПН,
    ВТ,
    СР,
    ЧТ,
    ПТ,
    СБ,
    ВС
}