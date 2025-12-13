# Copilot Instructions for Protelis

## Repository Overview

Protelis is a Java-hosted execution environment for Protelis programs, a language for aggregate programming. The project is organized into multiple Gradle modules:

- `protelis-interpreter`: Core interpreter for Protelis programs
- `protelis-lang`: Standard library and language constructs
- `protelis-test`: Testing utilities and test suites

## Language and Build System

- **Primary Languages**: Java and Kotlin
- **Build System**: Gradle with Kotlin DSL
- **Java Version Requirements**:
  - Java 17+ required to build the project
  - Compiled code runs on Java 8+ (backward compatible bytecode)

## Building and Testing

### Build Commands
```bash
./gradlew build          # Full build with tests and checks
./gradlew assemble       # Build without running tests
./gradlew shadowJar      # Create fat JAR
```

### Testing
```bash
./gradlew test           # Run all tests
./gradlew check          # Run all verification tasks (tests, linting, static analysis)
```

### Code Quality Checks
```bash
./gradlew ktlintCheck    # Check Kotlin code style
./gradlew ktlintFormat   # Auto-format Kotlin code
./gradlew detektMain     # Run Detekt static analysis on main code
./gradlew detektTest     # Run Detekt static analysis on test code
./gradlew spotbugsMain   # Run SpotBugs on main code
./gradlew spotbugsTest   # Run SpotBugs on test code
./gradlew checkstyleMain # Run Checkstyle on main code
./gradlew checkstyleTest # Run Checkstyle on test code
./gradlew pmdMain        # Run PMD on main code
./gradlew pmdTest        # Run PMD on test code
```

**Important**: Always run `./gradlew ktlintFormat` after modifying Kotlin files to ensure proper formatting.

## Code Style Guidelines

### Kotlin
- Use **4 spaces** for indentation (no tabs)
- Maximum line length: **120 characters**
- Follow IntelliJ IDEA code style (`ktlint_code_style=intellij_idea`)
- Insert final newline in all files
- Code style is enforced by ktlint - run `./gradlew ktlintFormat` before committing

### Java
- Use **spaces instead of tabs**
- Follow existing code style
- Code is checked by Checkstyle, PMD, and SpotBugs

### General
- Line delimiter: **LF (Unix-style)** only, even on Windows
- All files should have a final newline
- No trailing whitespace

## Commit Message Conventions

This repository uses [Conventional Commits](https://www.conventionalcommits.org) for all contributions.

### Allowed Commit Types
- `feat`: New features (any scope allowed)
- `fix`: Bug fixes (any scope allowed)
- `docs`: Documentation changes (any scope allowed)
- `test`: Changes related to tests
- `refactor`: Code refactoring without behavior changes
- `perf`: Performance improvements (any scope allowed)
- `style`: Code style updates
- `build`: Changes to build system
- `ci`: Changes to CI configuration
- `chore`: General maintenance
  - `chore(api-deps)`: API dependency updates
  - `chore(core-deps)`: Core dependency updates
  - `chore(deps)`: Generic dependency updates

### Scopes
Valid scopes are the Gradle module names: `interpreter`, `lang`, `test`. If a change spans multiple modules or modifies the root project, the scope may be omitted.

### Breaking Changes
Any commit type or scope suffixed with `!` denotes a **BREAKING CHANGE** (e.g., `feat!:`, `fix(lang)!:`).

### Examples
```
feat(interpreter): add support for new aggregate function
fix(lang): correct type checking for tuple operations
docs: update installation instructions in README
chore(deps): update gradle to version 9.2.1
```

## Development Workflow

### Git Flow
This project uses [git flow](https://github.com/nvie/gitflow):
- **Feature branches**: Create `feature-*` branches for new features
- **Fork and contribute**: Fork the repository, develop features, submit pull requests
- **Commit often**: Make small, frequent commits rather than large monolithic ones

### Pull Requests
- PR titles should follow Conventional Commits format
- All PRs should be assigned the `auto-update-rebase` label
- Typical PRs contain a single commit with a message matching the PR title
- Ensure all checks pass before requesting review

## Code Quality Standards

### Quality Requirements
- **All new code must comply with checker rules** (Checkstyle, PMD, SpotBugs, Detekt, ktlint)
- **Do not introduce new warnings**
- **Fix existing warnings** when working in an area (encouraged)
- Code is verified by the Gradle build system

### Static Analysis Tools
- **Checkstyle**: Java style checking
- **PMD**: Java code quality
- **SpotBugs**: Java bug detection
- **Detekt**: Kotlin static analysis
- **ktlint**: Kotlin linting and formatting

## Testing Practices

- Write tests for all new functionality
- Tests should follow existing patterns in the repository
- All tests must pass before merging
- Multi-JVM testing is supported (Java 8-25)
  - Tests can be run on various JVM versions to verify backward compatibility
  - Use `./gradlew testWithJvm<version>` to test on a specific Java version (e.g., `testWithJvm17`)

## Documentation

### Code Documentation
- Use appropriate Javadoc/KDoc comments for public APIs
- Follow existing documentation patterns
- Protelis code can be documented using the [protelisdoc Gradle plugin](https://plugins.gradle.org/plugin/org.protelis.protelisdoc)

### Project Documentation
- Keep README.md up-to-date with significant changes
- Documentation changes should use `docs:` commit type

## License and Copyright

- All new files must include the GPL v3 license header with the linking exception
- Copyright format for new files: "Copyright (C) <YEAR>, Danilo Pianini and contributors listed in the project's build.gradle.kts file."
- See LICENSE.txt for full license terms including the special linking exception
- Protelis is distributed under GPL v3 with a linking exception that allows combining with independent modules

## Versioning

This project follows [Semantic Versioning](http://semver.org/spec/v2.0.0.html):
- **Major version**: Breaking changes (incompatible API changes)
- **Minor version**: New features, backwards compatible
- **Patch version**: Bug fixes, no API changes

Releases are automated and occur frequently.
