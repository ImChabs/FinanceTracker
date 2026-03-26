# Validation Report

Current block
- Name: Dashboard inactive entries visibility fix
- Scope: Keep inactive recurring entries visible and editable from the dashboard while preserving active-only totals and upcoming payments.

Loop 1
- Validation target: `bash scripts/validate-unit-tests.sh`
- Underlying command: `:app:testDebugUnitTest`
- Why this target: Dashboard state mapping changed and the affected scope includes unit-test coverage for that mapper.
- Final status: not_run
- Attempts used: 1/3
- Run 1: Blocked by environment. The default Gradle home under `/home/ruyebran/.gradle` is read-only in this sandbox, so the wrapper could not create its lock file.
- Run 2: Not used.
- Run 3: Not used.
- In-scope fixes applied: Retried with `GRADLE_USER_HOME=/tmp/gradle-home`, which moved the failure forward but exposed a second environment blocker.
- Outstanding issues: The sandbox also blocks network access, so the wrapper could not download `gradle-9.4.1-bin.zip` into the temporary Gradle home.

Loop 2
- Validation target: `bash scripts/validate-compile.sh :app:compileDebugAndroidTestKotlin`
- Underlying command: `:app:compileDebugAndroidTestKotlin`
- Why this target: The change set also updated a dashboard Compose UI test, so the smallest additional verification is targeted androidTest Kotlin compilation.
- Final status: not_run
- Attempts used: 1/3
- Run 1: Blocked by environment for the same Gradle-wrapper reasons as Loop 1: read-only default Gradle home, then network-restricted wrapper download when redirected to `/tmp`.
- Run 2: Not used.
- Run 3: Not used.
- In-scope fixes applied: None beyond the `GRADLE_USER_HOME=/tmp/gradle-home` retry recorded above.
- Outstanding issues: Verification needs a writable Gradle home that already contains the required Gradle distribution, or a network-enabled environment.
