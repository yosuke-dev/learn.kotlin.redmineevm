package learn.kotlin.redmineevm.domain.value

import java.lang.IllegalArgumentException

data class DoneRatio(val value: Int) {
    companion object {
        private const val MIN_VALUE = 0
        private const val MAX_VALUE = 100
    }

    init {
        if (value < MIN_VALUE) throw IllegalArgumentException("value must be greater than or equal to $MIN_VALUE.")
        if (value > MAX_VALUE) throw IllegalArgumentException("value must be less than or equal to $MAX_VALUE.")
    }

    fun isMax(): Boolean {
        return value == MAX_VALUE
    }

    operator fun plus(other: DoneRatio) = DoneRatio(value + other.value)
    operator fun minus(other: DoneRatio) = DoneRatio(value - other.value)
    operator fun times(other: DoneRatio) = DoneRatio(value * other.value)
    operator fun div(other: DoneRatio) = DoneRatio(value / other.value)

    operator fun compareTo(other: DoneRatio) = value.compareTo(other.value)
}
