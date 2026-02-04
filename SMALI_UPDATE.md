# Smali/Baksmali Update to Android 16 Support

## Changes Made

This document describes the updates made to support the latest Android versions using the official Google smali/baksmali libraries.

### Version Update

**Previous Version:**
- Source: JitPack repository (`com.github.iBotPeaches.smali`)
- Version: Commit hash `b6365a84f4`
- API Support: Capped at API 29 (Android 10)

**New Version:**
- Source: Google Maven repository (`com.android.tools.smali`)
- Version: `3.0.9` (latest official release)
- API Support: Up to API 35 (Android 15)

### Files Modified

1. **gradle/libs.versions.toml**
   - Updated `baksmali` version from `b6365a84f4` to `3.0.9`
   - Updated `smali` version from `b6365a84f4` to `3.0.9`
   - Changed module from `com.github.iBotPeaches.smali` to `com.android.tools.smali`

2. **build.gradle.kts**
   - Removed JitPack repository configuration
   - Now uses Google Maven repository exclusively for smali/baksmali dependencies

3. **brut.apktool/apktool-lib/src/main/java/brut/androlib/smali/SmaliBuilder.java**
   - Removed API 29 hardcoded limitation
   - Updated constructor to accept full API level without capping
   - Added comment about current support (API 35) and future Android 16+ support

### AOSP Source Verification

The changes were verified against the official Android Open Source Project (AOSP) repository:

**Repository:** https://android.googlesource.com/platform/external/google-smali
**Tag:** android-16.0.0_r4
**Commit:** ff32c1014f9b235607576320309a03c7f5030d4e
**Version in AOSP:** 3.0.7

### API Level Support

| Android Version | API Level | DEX Version | Status |
|-----------------|-----------|-------------|--------|
| Android 6 (M) | 23 | 35 | ✅ Supported |
| Android 7 (N) | 24-25 | 37 | ✅ Supported |
| Android 8 (O) | 26-27 | 38 | ✅ Supported |
| Android 9 (P) | 28 | 39 | ✅ Supported |
| Android 10 (Q) | 29 | 39 | ✅ Supported |
| Android 11 (R) | 30 | 40 | ✅ Supported |
| Android 12 (S) | 31-32 | 40 | ✅ Supported |
| Android 13 (T) | 33 | 40 | ✅ Supported |
| Android 14 (U) | 34 | 40 | ✅ Supported |
| Android 15 (V) | 35 | 41 | ✅ Supported |
| Android 16 | 36 | TBD | ⏳ Pending Google release |

### Key Improvements

1. **Official Support**: Now using officially maintained Google libraries instead of custom forks
2. **Extended API Support**: Removed artificial API 29 limitation
3. **Future-Proof**: Ready for Android 16 support when Google releases updated smali version
4. **Better Maintenance**: Easier to update as Google releases new versions on Maven
5. **Build Stability**: Using stable, released versions instead of commit hashes

### Building and Testing

The project successfully builds with the new dependencies:
```bash
./gradlew clean build shadowJar
```

All existing tests pass:
```bash
./gradlew test
```

### Future Updates

When Android 16 (API 36) support is released by Google:
1. Update `gradle/libs.versions.toml` with the new smali/baksmali version number
2. No code changes should be needed in SmaliBuilder.java (limitation already removed)
3. Test with Android 16 APKs to verify compatibility

### References

- Official Google smali repository: https://github.com/google/smali
- AOSP external/google-smali: https://android.googlesource.com/platform/external/google-smali
- Google Maven: https://maven.google.com/web/index.html?q=smali
- Smali artifacts: `com.android.tools.smali:smali` and `com.android.tools.smali:smali-baksmali`
