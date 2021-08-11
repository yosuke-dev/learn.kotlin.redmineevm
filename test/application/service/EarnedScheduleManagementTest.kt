package learn.kotlin.redmineevm.application.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import learn.kotlin.redmineevm.domain.model.EarnedSchedule
import learn.kotlin.redmineevm.domain.model.RedmineIssue
import learn.kotlin.redmineevm.domain.model.RedmineJournal
import learn.kotlin.redmineevm.domain.model.RedmineTimeEntry
import learn.kotlin.redmineevm.domain.value.*
import learn.kotlin.redmineevm.infrastructure.externalapi.RedmineIssueApi
import learn.kotlin.redmineevm.infrastructure.externalapi.RedmineTimeEntryApi
import java.time.LocalDate

class EarnedScheduleManagementTest : FunSpec() {
    init {
        val issues = getTestDataIssues()

        val timeEntries1 = getTestDataTimeEntries(1)
        val timeEntries2 = getTestDataTimeEntries(2)
        val timeEntries3 = getTestDataTimeEntries(3)

        context("getEarnedValueSchedules") {
            test("child exists in issue") {
                val moto = EarnedSchedule(EarnedScheduleDate("2021-01-01"), BudgetAtCompletion(170.5F), PlannedValue(4.1F), ActualCost(2.5F), EarnedValue(0F))
                val actual = listOf(
                    moto,
                    moto.copy(earnedScheduleDate = EarnedScheduleDate("2021-01-02"), plannedValue = PlannedValue(41.54F), actualCost = ActualCost(32.5F), earnedValue = EarnedValue(28.49F)),
                    moto.copy(earnedScheduleDate = EarnedScheduleDate("2021-01-03"), plannedValue = PlannedValue(95.64F), actualCost = ActualCost(92.5F), earnedValue = EarnedValue(113.74F)),
                    moto.copy(earnedScheduleDate = EarnedScheduleDate("2021-01-04"), plannedValue = PlannedValue(149.74F), actualCost = ActualCost(122.5F), earnedValue = EarnedValue(156.36F)),
                    moto.copy(earnedScheduleDate = EarnedScheduleDate("2021-01-05"), plannedValue = PlannedValue(170.5F), actualCost = ActualCost(134F), earnedValue = EarnedValue(170.5F)),
                )
                val redmineIssueApi = mockk<RedmineIssueApi>()
                val redmineTimeEntryApi = mockk<RedmineTimeEntryApi>()
                val service = EarnedScheduleManagement(redmineIssueApi, redmineTimeEntryApi)

                every { redmineIssueApi.getIssuesInJournalsByVersionId(104) } returns issues
                every { redmineTimeEntryApi.getTimeEntriesByIssueId(IssueId(1)) } returns timeEntries1
                every { redmineTimeEntryApi.getTimeEntriesByIssueId(IssueId(2)) } returns timeEntries2
                every { redmineTimeEntryApi.getTimeEntriesByIssueId(IssueId(3)) } returns timeEntries3

                val result = service.getEarnedValueSchedules(104)
                result shouldBe actual
            }
        }
    }

    private fun getTestDataIssues(): List<RedmineIssue>{
        val parent = RedmineIssue(
            IssueId(1),
            null,
            StartDate(LocalDate.of(2021, 1, 1)),
            DueDate(LocalDate.of(2021, 1, 5)),
            CloseOn(LocalDate.of(2021, 1, 5)),
            EstimatedHour(20.5F),
            listOf(
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 2)), DoneRatio(50)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 5)), DoneRatio(100)),
            ),
        )
        val child = parent.copy(
            id = IssueId(2),
            parent = parent.id,
            startDate = StartDate(LocalDate.of(2021, 1, 2)),
            dueDate = DueDate(LocalDate.of(2021, 1, 4)),
            closeOn = CloseOn(LocalDate.of(2021, 1, 4)),
            estimateHours = EstimatedHour(100F),
            journals = listOf(
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 2)), DoneRatio(25)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 3)), DoneRatio(75)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 4)), DoneRatio(100)),
            ),
        )
        val childSecond = child.copy(
            id = IssueId(3),
            startDate = StartDate(LocalDate.of(2021, 1, 3)),
            dueDate = DueDate(LocalDate.of(2021, 1, 5)),
            closeOn = CloseOn(LocalDate.of(2021, 1, 5)),
            estimateHours = EstimatedHour(50F),
            journals = listOf(
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 3)), DoneRatio(50)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 4)), DoneRatio(75)),
                RedmineJournal(CreateOn(LocalDate.of(2021, 1, 5)), DoneRatio(100)),
            ),
        )
        return listOf(parent, child, childSecond)
    }

    private fun getTestDataTimeEntries(number: Int): List<RedmineTimeEntry>{
        return when (number) {
            1 -> listOf(
                RedmineTimeEntry(IssueId(1), SpentOn(LocalDate.of(2021, 1, 1)), ActualCost(2.5F)),
                RedmineTimeEntry(IssueId(1), SpentOn(LocalDate.of(2021, 1, 5)), ActualCost(4F)),
            )
            2 -> listOf(
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 2)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 2)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 4)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(2), SpentOn(LocalDate.of(2021, 1, 4)), ActualCost(7.5F)),
            )
            3 -> listOf(
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 2)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 2)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 3)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 4)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 4)), ActualCost(7.5F)),
                RedmineTimeEntry(IssueId(3), SpentOn(LocalDate.of(2021, 1, 5)), ActualCost(7.5F)),
            )
            else -> listOf<RedmineTimeEntry>()
        }
    }
}