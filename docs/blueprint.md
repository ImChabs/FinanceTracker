# Personal Finance Tracker Blueprint

## Purpose

Personal Finance Tracker is a simple Android app for tracking **subscriptions** and **recurring expenses** in one place.

Its main goal is to help the user understand:
- what recurring payments are currently active
- what payments are coming next
- how much recurring spending exists per month

This project is primarily a **learning + portfolio** app, so the product direction should stay practical, polished, and intentionally limited in scope.

---

## Product Direction

The app should prioritize:
1. **Clear visibility of recurring financial commitments**
2. **Reliable local data management**
3. **Simple, polished user experience**
4. **Clean architecture and realistic implementation patterns**
5. **Block-by-block delivery without losing long-term direction**

This is **not** intended to become a full banking app, budgeting platform, or accounting tool.

---

## Core Product Scope

The core product is focused on **subscriptions + recurring expenses only**.

Examples:
- streaming subscriptions
- software subscriptions
- gym memberships
- insurance payments
- rent-like recurring obligations
- utilities or other repeated monthly charges

The app should treat these as recurring entries that the user can manage locally.

---

## MVP Scope

The first meaningful version of the app should include:

- a dashboard with a simple financial summary
- a list of active recurring entries
- a list of upcoming payments
- the ability to add a recurring entry
- the ability to edit a recurring entry
- the ability to delete a recurring entry
- category support
- monthly total / recurring cost summary

Each recurring entry should conceptually support fields such as:
- name
- type (subscription or recurring expense)
- amount
- billing frequency
- next payment date
- category
- active status
- optional notes

Exact field design can evolve during implementation, but the product should remain centered on managing recurring obligations clearly and simply.

---

## Priorities

When tradeoffs appear, prioritize in this order:

1. **Core recurring-tracking usefulness**
2. **Good local persistence and data flow**
3. **Clear dashboard and upcoming-payment visibility**
4. **Simple, polished UX**
5. **Optional enhancements**

If a feature adds complexity without strongly improving the core recurring-tracking experience, it should likely be postponed.

---

## Out of Scope for Now

The following are intentionally out of scope for the current product direction:

- authentication
- backend services
- cloud sync
- one-time expense tracking
- bank integrations
- shared or multi-user finance
- complex budgeting systems
- advanced analytics
- highly detailed reporting
- monetization / ads

These can be revisited in the future, but they should not shape current implementation decisions.

---

## Remote Data / API Direction

The app is primarily **local-first**, but it should include at least one lightweight remote-data flow to practice realistic app architecture.

Recommended remote use case:
- fetch **currency metadata or exchange-rate data** from a public API
- map the remote response into app models
- cache the result locally
- expose it through the repository/data layer

This remote integration exists mainly to practice:
- Retrofit setup
- DTO-to-domain/data mapping
- error handling
- local caching with Room
- real-world sync patterns

Important:
- remote data is a **supporting technical feature**, not the central product feature
- the app should remain usable without depending on continuous network access
- full multi-currency finance tracking is **not** a core goal right now

---

## Roadmap

### Phase 1 — Foundation
Establish the application shell and project structure.

Typical outcomes:
- app entry point
- navigation foundation
- placeholder screens
- design/theme baseline
- basic package organization

### Phase 2 — Local Data Foundation
Introduce the core local model for recurring entries.

Typical outcomes:
- entity/model structure
- Room database setup
- DAO contracts
- repository foundation
- mapping between layers

### Phase 3 — Recurring Entry Management
Implement the core CRUD workflows.

Typical outcomes:
- create recurring entry
- edit recurring entry
- delete recurring entry
- validate user input
- support categories and recurring metadata

### Phase 4 — Main Product Experience
Build the screens that make the app useful day to day.

Typical outcomes:
- dashboard summary
- active recurring entries list
- upcoming payments list
- monthly recurring total
- basic empty/error/loading states where relevant

### Phase 5 — Remote Sync Practice
Add a small but realistic API-driven feature.

Typical outcomes:
- Retrofit integration
- remote data source
- local cache of API results
- repository coordination between remote and local sources
- basic sync/error handling behavior

### Phase 6 — Polish and Optional Enhancements
Improve the project without expanding scope too aggressively.

Possible outcomes:
- small UX refinements
- better sorting/filtering
- reminders/notifications
- lightweight statistics
- settings
- quality improvements and cleanup

---

## Block Strategy

Implementation should happen in **small blocks**, but each block should clearly support one of the roadmap phases above.

This blueprint is meant to provide the persistent product direction across multiple block-based chats:
- blocks define the immediate implementation step
- this blueprint defines the broader product destination and scope boundaries

If a future block conflicts with this blueprint, prefer the simpler interpretation unless the product direction is intentionally updated.

---

## Success Criteria

The project is successful if it becomes:

- a clean, portfolio-quality Android app
- useful for personal tracking of subscriptions and recurring expenses
- simple enough to stay maintainable
- realistic enough to demonstrate solid Android architecture and workflows
- flexible enough to support block-by-block development without losing direction

