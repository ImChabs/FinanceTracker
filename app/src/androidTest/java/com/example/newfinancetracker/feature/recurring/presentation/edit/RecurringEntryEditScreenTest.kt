package com.example.newfinancetracker.feature.recurring.presentation.edit

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RecurringEntryEditScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun recurringEntryEditScreen_loadingStateExposesMergedAccessibilitySummaryAndStateDescription() {
        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = true
            )
        )

        composeRule.assertSingleStateCard("Loading recurring entry details.")
            .assert(hasStateDescription("Recurring entry details are loading"))
            .assertIsDisplayed()
        composeRule.onNodeWithText("Loading recurring entry...")
            .assertExists()
    }

    @Test
    fun recurringEntryEditScreen_missingEntryStateExposesMergedAccessibilitySummaryAndActionLabel() {
        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isMissingEntry = true
            )
        )

        composeRule.assertSingleStateCard(
            "This recurring entry is no longer available. " +
                "This recurring entry could not be found. It may have been removed or changed elsewhere."
        ).assert(hasStateDescription("Recurring entry unavailable"))
            .assertIsDisplayed()
        composeRule.onNodeWithText("This recurring entry is no longer available")
            .assertExists()
            .assertIsDisplayed()
        composeRule.onNodeWithText(
            "This recurring entry could not be found. It may have been removed or changed elsewhere."
        ).assertExists()
        composeRule.onNodeWithText("Back to dashboard")
            .assertExists()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Back to dashboard")
            .assert(hasClickAction())
            .assert(hasClickLabel("Return to the dashboard"))
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
    fun recurringEntryEditScreen_deleteConfirmationExposesAccessibilitySummaryStateAndActionLabels() {
        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isDeleteConfirmationVisible = true
            )
        )

        composeRule.onNode(
            hasContentDescription(
                "Delete this recurring entry? This will permanently remove this recurring entry from your dashboard."
            )
        ).assertExists()
            .assert(hasPaneTitle("Delete this recurring entry?"))
            .assert(hasStateDescription("Delete confirmation dialog"))
            .assertIsDisplayed()
        composeRule.onNodeWithText("Delete")
            .assert(hasClickAction())
            .assert(hasClickLabel("Delete this recurring entry permanently"))
        composeRule.onNodeWithText("Keep entry")
            .assert(hasClickAction())
            .assert(hasClickLabel("Keep this recurring entry"))
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
    fun recurringEntryEditScreen_deleteConfirmationDismissRequestDispatchesDeleteDismissedAction() {
        val actions = mutableListOf<RecurringEntryEditAction>()

        composeRule.setRecurringEntryEditContent(
            state = RecurringEntryEditState(
                entryId = 7L,
                isLoading = false,
                isDeleteConfirmationVisible = true
            ),
            onAction = actions::add
        )

        composeRule.onNode(
            hasContentDescription(
                "Delete this recurring entry? This will permanently remove this recurring entry from your dashboard."
            )
        ).assertExists()
            .assert(hasDismissAction())
            .performSemanticsAction(SemanticsActions.Dismiss)

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

    private fun ComposeContentTestRule.assertSingleStateCard(
        contentDescription: String
    ): SemanticsNodeInteraction = onAllNodes(hasContentDescription(contentDescription))
        .assertCountEquals(1)
        .let { onNode(hasContentDescription(contentDescription)) }

    private fun hasStateDescription(description: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, description)

    private fun hasPaneTitle(title: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.PaneTitle, title)

    private fun hasClickLabel(label: String): SemanticsMatcher =
        SemanticsMatcher("has click label $label") { semanticsNode ->
            val config = semanticsNode.config
            if (SemanticsActions.OnClick !in config) {
                false
            } else {
                config[SemanticsActions.OnClick].label == label
            }
        }

    private fun hasDismissAction(): SemanticsMatcher =
        SemanticsMatcher.keyIsDefined(SemanticsActions.Dismiss)
}
