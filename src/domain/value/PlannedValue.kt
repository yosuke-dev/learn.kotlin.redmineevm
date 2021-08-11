package learn.kotlin.redmineevm.domain.value

import java.lang.IllegalArgumentException

data class PlannedValue(val value: Float){
    companion object {
        private const val MIN_VALUE = 0F
    }

    init {
        if (value < MIN_VALUE) throw IllegalArgumentException("value must be greater than or equal to $MIN_VALUE.")
    }

    operator fun compareTo(other: PlannedValue) = value.compareTo(other.value)
}
