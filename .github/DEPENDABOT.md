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
- **Registries**: Google Maven (for Android/smali dependencies)
- **Monitored Files**:
  - `gradle/libs.versions.toml` (version catalog)
  - All `build.gradle.kts` files
  - `gradle/wrapper/gradle-wrapper.properties` (Gradle wrapper)

## Configuration Details

### Custom Registries

Dependabot is configured to use additional Maven registries beyond Maven Central:

- **Google Maven** (`https://dl.google.com/android/maven2`)
  - Required for Android tools and smali/baksmali dependencies
  - Monitors packages: `com.android.tools.smali:*`, `com.android.tools:*`
  
This ensures Dependabot can find and suggest updates for all dependencies, including those published to Google's Maven repository.

### Pull Request Settings
- **Maximum Open PRs**: 10 per ecosystem
- **Rebase Strategy**: Automatic - PRs are automatically rebased when the base branch is updated
- **Commit Message Format**: `chore(deps): update <dependency-name>`

### Dependency Groups

To efficiently manage updates and stay within the PR limit, related dependencies are grouped together:

1. **Apache Commons** (`apache-commons`)
   - commons-io, commons-cli, commons-lang3, commons-text
   - Updates all Apache Commons libraries together in a single PR

2. **Google Dependencies** (`google-dependencies`)
   - guava, r8
   - Updates Google-maintained libraries together

3. **XML Dependencies** (`xml-dependencies`)
   - xmlunit, xmlpull
   - Updates XML processing libraries together

4. **Smali Tools** (`smali-tools`)
   - baksmali, smali, dexlib2
   - Updates all Android smali tools together

5. **Development Dependencies** (`dev-dependencies`)
   - junit, maven-publish plugin
   - Updates testing and build tools together

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

For manual dependency updates when needed:

1. **Gradle Wrapper**: Run `./gradlew wrapper --gradle-version=X.Y.Z`
2. **Other Dependencies**: Update versions in `gradle/libs.versions.toml`

## Additional Resources

- [Dependabot Documentation](https://docs.github.com/en/code-security/dependabot)
- [Dependabot Configuration Options](https://docs.github.com/en/code-security/dependabot/dependabot-version-updates/configuration-options-for-the-dependabot.yml-file)
- [Project Dependencies](../gradle/libs.versions.toml)
