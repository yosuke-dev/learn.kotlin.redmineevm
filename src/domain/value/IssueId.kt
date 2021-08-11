package learn.kotlin.redmineevm.domain.value

import java.lang.IllegalArgumentException

data class IssueId(val value: Int){
    companion object {
        private const val MIN_VALUE = 0
    }

    init {
        if (value < MIN_VALUE) throw IllegalArgumentException("value must be greater than or equal to $MIN_VALUE.")
    }

    operator fun compareTo(other: IssueId) = value.compareTo(other.value)
}
