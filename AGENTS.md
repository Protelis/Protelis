# Development Guidelines

This repository uses [Conventional Commits](https://www.conventionalcommits.org) for all contributions.

## Allowed Commit Types

- `feat`: Use for new features. Any scope is allowed.
- `chore(api-deps)`: Use when updating API dependencies.
- `fix`: Use for bug fixes. Any scope is allowed.
- `docs`: Use for documentation changes. Any scope is allowed.
- `perf`: Use for performance improvements. Any scope is allowed.
- `revert`: Use for reverting previous commits. Any scope is allowed.
- `chore(core-deps)`: Use when updating core dependencies.
- `test`: Use for changes related to tests.
- `ci`: Use for changes related to continuous integration.
- `build`: Use for changes related to the build system.
- `chore(deps)`: Use when updating generic dependencies.
- `chore`: Use for general maintenance with scopes other than the ones above.
- `style`: Use for code style updates.
- `refactor`: Use for refactoring existing code without changing behaviour.

Any commit type or scope suffixed with `!` denotes a **BREAKING CHANGE**.

## Scopes

Valid scopes are the names of the Gradle modules of this project:
`interpreter`, `lang`, and `test`.
If a change spans multiple modules or modifies the root project, the scope may be omitted.
The dependency update scopes `api-deps`, `core-deps`, and `deps` are also allowed for the `chore` type.

## Pull requests

Pull request titles should be formatted as conventional commits.
A typical good pull request will contain a single commit whose message first line is the same of the PR title.
All pull requests should be assigned the `auto-update-rebase` label to let mergify keep them in sync with the default branch.

## Verification

Always run:
```bash
./gradlew ktlintF
```
After a modification of a Kotlin file to ensure that formatting is correct.

Before submitting a pull request, verify the project builds successfully:
```bash
./gradlew build assemble
```
This process can take quite some time. If it takes too long, faster verification can be performed using:
```bash
./gradlew ktlintCheck detektMain detektTest test
```
