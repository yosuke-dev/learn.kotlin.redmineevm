package learn.kotlin.redmineevm.domain.value

import java.lang.IllegalArgumentException

data class EarnedScheduleDate(val value: String){
    companion object {
        private const val REGEX_VALUE = """^[0-9]{4}[/|-](0[1-9]|1[0-2])[/|-](0[1-9]|[12][0-9]|3[01])$"""
    }

    init {
        val regex = Regex(REGEX_VALUE)
        if (!regex.matches(value)) throw IllegalArgumentException("value must be 'yyyy/MM/dd' or 'yyyy-MM-dd'.")
    }

    operator fun compareTo(other: EarnedScheduleDate) = value.compareTo(other.value)
}
