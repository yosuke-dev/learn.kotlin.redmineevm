package learn.kotlin.redmineevm.presentation.response

data class EarnedScheduleResponse(
    val earnedScheduleDate: String,
    val budgetAtCompletion: Float,
    val plannedValue: Float,
    val actualCost: Float,
    val earnedValue: Float
)