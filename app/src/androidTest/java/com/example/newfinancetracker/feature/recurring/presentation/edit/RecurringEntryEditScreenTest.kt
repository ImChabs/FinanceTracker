package com.example.newfinancetracker.feature.recurring.presentation.edit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RecurringEntryEditScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun recurringEntryEditScreen_loadingStateShowsLoadingCopy() {
        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = true
            )
        )

        composeRule.onNodeWithText("Loading recurring entry...")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun recurringEntryEditScreen_missingEntryStateShowsMissingCopy() {
        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isMissingEntry = true
            )
        )

        composeRule.onNodeWithText("This recurring entry is no longer available")
            .assertExists()
            .assertIsDisplayed()
        composeRule.onNodeWithText(
            "This recurring entry could not be found. It may have been removed or changed elsewhere."
        ).assertExists()
        composeRule.onNodeWithText("Back to dashboard")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun recurringEntryEditScreen_deleteConfirmationShowsDialogCopy() {
        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isDeleteConfirmationVisible = true
            )
        )

        composeRule.onNodeWithText("Delete this recurring entry?")
            .assertExists()
            .assertIsDisplayed()
        composeRule.onNodeWithText(
            "This will permanently remove this recurring entry from your dashboard."
        ).assertExists()
        composeRule.onNodeWithText("Delete")
            .assertExists()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Keep entry")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun recurringEntryEditScreen_deleteButtonDispatchesDeleteClickedAction() {
        val actions = mutableListOf<RecurringEntryEditAction>()

        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false
            ),
            onAction = actions::add
        )

        composeRule.onNodeWithText("Delete recurring entry")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        assertEquals(listOf(RecurringEntryEditAction.DeleteClicked), actions)
    }

    @Test
    fun recurringEntryEditScreen_deleteConfirmationConfirmDispatchesDeleteConfirmedAction() {
        val actions = mutableListOf<RecurringEntryEditAction>()

        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isDeleteConfirmationVisible = true
            ),
            onAction = actions::add
        )

        composeRule.onNodeWithText("Delete")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        assertEquals(listOf(RecurringEntryEditAction.DeleteConfirmed), actions)
    }

    @Test
    fun recurringEntryEditScreen_deleteConfirmationDismissDispatchesDeleteDismissedAction() {
        val actions = mutableListOf<RecurringEntryEditAction>()

        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isDeleteConfirmationVisible = true
            ),
            onAction = actions::add
        )

        composeRule.onNodeWithText("Keep entry")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        assertEquals(listOf(RecurringEntryEditAction.DeleteDismissed), actions)
    }

    @Test
    fun recurringEntryEditScreen_missingEntryBackActionInvokesNavigateBackCallback() {
        var navigateBackCount = 0

        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isMissingEntry = true
            ),
            onNavigateBack = { navigateBackCount += 1 }
        )

        composeRule.onNodeWithText("Back to dashboard")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        assertEquals(1, navigateBackCount)
    }

    private fun ComposeContentTestRule.setRecurringEntryEditContent(
        state: RecurringEntryEditState,
        onAction: (RecurringEntryEditAction) -> Unit = {},
        onNavigateBack: () -> Unit = {}
    ) {
        setContent {
            FinanceTrackerTheme {
                RecurringEntryEditScreen(
                    state = state,
                    onAction = onAction,
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}
