package learn.kotlin.redmineevm.domain.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import learn.kotlin.redmineevm.domain.model.RedmineIssue
import learn.kotlin.redmineevm.domain.model.RedmineJournal
import learn.kotlin.redmineevm.domain.value.*
import java.time.LocalDate

class DoneRatioServiceTest : FunSpec() {
    init {
        val target = RedmineIssue(
            IssueId(1),
            null,
            StartDate(LocalDate.of(2021, 1, 1)),
            DueDate(LocalDate.of(2021, 1, 5)),
            CloseOn(LocalDate.of(2021, 1, 5)),
            EstimatedHour(20.5F),
            listOf(
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 2)), DoneRatio(50)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 3)), DoneRatio(100)),
            ),
        )
        val childTarget = target.copy(
            id = IssueId(2),
            parent = target.id,
            estimateHours = EstimatedHour(100F),
            journals = listOf(
                RedmineJournal(CreateOn(LocalDate.of(2021, 1,2)), DoneRatio(25)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1,3)), DoneRatio(75)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1,4)), DoneRatio(100)),
            ),
        )
        val childSecond = childTarget.copy(
            id = IssueId(3),
            estimateHours = EstimatedHour(50F),
            journals = listOf(
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 3)), DoneRatio(50)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 4)), DoneRatio(75)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 5)), DoneRatio(100)),
            ),
        )
        val issues = listOf(target, childTarget, childSecond)

        context("calculate"){
            test("child exists in issue"){
                val testParams = listOf(
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,1), 0),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,2), 17),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,3), 67),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,4), 92),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,5), 100),
                )
                for (param in testParams){
                    val result = DoneRatioService.calculate(issues, target, param.currentDate)
                    result.value shouldBe param.actual
                }
            }

            test("child does not exist in issue"){
                val testParams = listOf(
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,1), 0),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,2), 25),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,3), 75),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,4), 100),
                    DoneRatioServiceTestParam(LocalDate.of(2021, 1,5), 100),
                )

                for (param in testParams){
                    val result = DoneRatioService.calculate(issues, childTarget, param.currentDate)
                    result.value shouldBe param.actual
                }
            }
        }
    }
}