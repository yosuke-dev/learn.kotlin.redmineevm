package learn.kotlin.redmineevm.domain.model

import learn.kotlin.redmineevm.domain.value.DoneRatio
import learn.kotlin.redmineevm.domain.value.EarnedValue
import learn.kotlin.redmineevm.domain.value.EstimatedHour

data class RedmineChildIssue(
    val doneRatio: DoneRatio,
    var estimateHour: EstimatedHour?,
    var earnedValue: EarnedValue = EarnedValue(0F),
)
