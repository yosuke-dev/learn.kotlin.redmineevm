package learn.kotlin.redmineevm.domain.model

import learn.kotlin.redmineevm.domain.value.CreateOn
import learn.kotlin.redmineevm.domain.value.DoneRatio

data class RedmineJournal(
    val createOn: CreateOn,
    val doneRatio: DoneRatio
)
