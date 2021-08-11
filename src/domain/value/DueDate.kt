package learn.kotlin.redmineevm.domain.value

import java.time.LocalDate

data class DueDate(val value: LocalDate) {
    operator fun compareTo(value: DueDate): Int {
        return this.value.compareTo(value.value)
    }
}