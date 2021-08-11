package learn.kotlin.redmineevm.domain.value

import java.time.LocalDate

data class CloseOn(val value: LocalDate) {
    operator fun compareTo(value: CloseOn) = this.value.compareTo(value.value)
}