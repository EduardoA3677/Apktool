# Dependabot Configuration

This repository uses [GitHub Dependabot](https://docs.github.com/en/code-security/dependabot) to automatically keep dependencies up to date.

## Overview

Dependabot is configured to monitor and update the following dependency types:

### 1. GitHub Actions Dependencies
- **Frequency**: Weekly (Mondays at 4:00 AM EST)
- **Purpose**: Keeps GitHub Actions workflows up to date with the latest action versions
- **Labels**: `dependencies`, `github-actions`
- **Monitored Files**: 
  - `.github/workflows/*.yml`

### 2. Gradle Dependencies
- **Frequency**: Weekly (Mondays at 4:00 AM EST)
- **Purpose**: Keeps Java/Kotlin library dependencies up to date
- **Labels**: `dependencies`, `gradle`
- **Monitored Files**:
  - `gradle/libs.versions.toml` (version catalog)
  - All `build.gradle.kts` files
  - `gradle/wrapper/gradle-wrapper.properties` (Gradle wrapper)

## Configuration Details

### Pull Request Settings
- **Maximum Open PRs**: 10 per ecosystem
- **Rebase Strategy**: Automatic - PRs are automatically rebased when the base branch is updated
- **Commit Message Format**: `chore(deps): update <dependency-name>`

### Ignored Dependencies

Some dependencies are configured to be ignored or have limited update types:

1. **Smali/Baksmali Libraries** (`com.github.iBotPeaches.smali:*`)
   - **Reason**: These use custom builds from JitPack due to upstream issues
   - **Ignored Updates**: Major and minor version updates
   - **Action Required**: These should be updated manually after verifying compatibility
   - **Reference**: See comments in `gradle/libs.versions.toml` for more details

## How It Works

1. **Automatic Scanning**: Dependabot scans the repository weekly on Mondays
2. **PR Creation**: When updates are available, Dependabot creates pull requests
3. **Automated Tests**: GitHub Actions CI runs automatically on all Dependabot PRs
4. **Review & Merge**: Team members review and merge the PRs after tests pass

## Labels

All Dependabot PRs are automatically labeled for easy filtering:
- `dependencies` - All dependency updates
- `github-actions` - GitHub Actions updates specifically
- `gradle` - Gradle dependency updates specifically

## Manual Dependency Updates

For dependencies that need manual updates:

1. **Smali/Baksmali**: Update versions in `gradle/libs.versions.toml`
2. **Gradle Wrapper**: Run `./gradlew wrapper --gradle-version=X.Y.Z`

## Additional Resources

- [Dependabot Documentation](https://docs.github.com/en/code-security/dependabot)
- [Dependabot Configuration Options](https://docs.github.com/en/code-security/dependabot/dependabot-version-updates/configuration-options-for-the-dependabot.yml-file)
- [Project Dependencies](../gradle/libs.versions.toml)
