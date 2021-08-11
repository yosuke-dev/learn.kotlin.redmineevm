package learn.kotlin.redmineevm.domain.model

import learn.kotlin.redmineevm.domain.value.*

data class EarnedSchedule(
    val earnedScheduleDate: EarnedScheduleDate,
    val budgetAtCompletion: BudgetAtCompletion,
    val plannedValue: PlannedValue,
    val actualCost: ActualCost,
    val earnedValue: EarnedValue
)