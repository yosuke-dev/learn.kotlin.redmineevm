package learn.kotlin.redmineevm.domain.value

import java.time.LocalDate

data class CreateOn(val value: LocalDate) {
    operator fun compareTo(value: CreateOn) = this.value.compareTo(value.value)
}