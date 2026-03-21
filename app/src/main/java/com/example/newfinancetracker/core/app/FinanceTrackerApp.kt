package com.example.newfinancetracker.core.app

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newfinancetracker.core.designsystem.theme.FinanceTrackerTheme
import com.example.newfinancetracker.core.navigation.FinanceTrackerNavHost

@Composable
fun FinanceTrackerApp(
    modifier: Modifier = Modifier
) {
    FinanceTrackerTheme {
        Surface(
            modifier = modifier,
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            FinanceTrackerNavHost()
        }
    }
}
