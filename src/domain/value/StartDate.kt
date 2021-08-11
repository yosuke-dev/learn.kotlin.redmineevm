package learn.kotlin.redmineevm.domain.value

import java.time.LocalDate

data class StartDate(val value: LocalDate) {
    operator fun compareTo(value: StartDate) = this.value.compareTo(value.value)
}