package tvz.filip.milkovic.smbraspored.shared.model

import java.time.DayOfWeek

enum class TypeOfDay {
    WEEK_DAY,
    SATURDAY,
    SUNDAY_OR_HOLIDAY;

    companion object {
        fun getTypeOfDayFromCurrentDay(dayOfWeek: DayOfWeek): TypeOfDay {
            var typeOfDay: TypeOfDay = WEEK_DAY
            if (dayOfWeek == DayOfWeek.SATURDAY) {
                typeOfDay = SATURDAY
            } else if (dayOfWeek == DayOfWeek.SUNDAY) {
                typeOfDay = SATURDAY
            }

            return typeOfDay
        }

        fun getNextDay(dayOfWeek: DayOfWeek): TypeOfDay {
            return when (dayOfWeek) {
                DayOfWeek.FRIDAY -> {
                    SATURDAY
                }
                DayOfWeek.SATURDAY -> {
                    SUNDAY_OR_HOLIDAY
                }
                DayOfWeek.SUNDAY -> {
                    WEEK_DAY
                }
                else -> WEEK_DAY
            }
        }
    }
}
