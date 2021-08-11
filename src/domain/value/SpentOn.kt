package learn.kotlin.redmineevm.domain.value

import java.time.LocalDate

data class SpentOn(val value: LocalDate) {
    operator fun compareTo(value: SpentOn) = this.value.compareTo(value.value)
}