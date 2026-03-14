package com.example.newfinancetracker.feature.recurring.data.local

import com.example.newfinancetracker.feature.recurring.domain.model.RecurringEntry

fun RecurringEntryEntity.toDomain(): RecurringEntry =
    RecurringEntry(
        id = id,
        name = name,
        amount = amount,
        billingFrequency = billingFrequency,
        nextPaymentDate = nextPaymentDate,
        category = category,
        type = type,
        isActive = isActive,
        notes = notes
    )

fun RecurringEntry.toEntity(): RecurringEntryEntity =
    RecurringEntryEntity(
        id = id,
        name = name,
        amount = amount,
        billingFrequency = billingFrequency,
        nextPaymentDate = nextPaymentDate,
        category = category,
        type = type,
        isActive = isActive,
        notes = notes
    )
