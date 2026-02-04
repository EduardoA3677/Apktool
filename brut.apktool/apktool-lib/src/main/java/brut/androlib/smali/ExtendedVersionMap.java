/*
 *  Copyright (C) 2025 Connor Tumbleson <connor.tumbleson@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package brut.androlib.smali;

/**
 * Extension of smali's VersionMap to provide forward compatibility information
 * for future Android versions that may not yet be supported by the smali library.
 * 
 * This class provides best-effort mapping for Android versions beyond what the
 * current smali library officially supports.
 */
public class ExtendedVersionMap {
    
    /**
     * Maps an API level to its expected DEX version.
     * This extends the mapping provided by smali's VersionMap to include
     * predictions for future Android versions.
     * 
     * @param api The Android API level
     * @return The DEX version, or -1 if unknown
     */
    public static int mapApiToDexVersion(int api) {
        // Use smali's official mapping for supported versions (API <= 35)
        if (api <= 35) {
            // These mappings match smali's VersionMap:
            if (api <= 23) return 35;  // Android M/6
            switch (api) {
                case 24:  // Android N/7
                case 25:  // Android N/7.1
                    return 37;
                case 26:  // Android O/8
                case 27:  // Android O/8.1
                    return 38;
                case 28:  // Android P/9
                case 29:  // Android Q/10
                    return 39;
                case 30:  // Android R/11
                case 31:  // Android S/12
                case 32:  // Android S/12.1
                case 33:  // Android T/13
                case 34:  // Android U/14
                    return 40;
                case 35:  // Android V/15
                    return 41;
            }
        }
        
        // Predictions for future versions (based on historical patterns):
        // Note: These are educated guesses and should be updated when official
        // information becomes available
        if (api == 36) {
            // Android 16 is expected to use DEX v42
            // This is based on the pattern of incrementing DEX version with major features
            return 42;
        }
        
        // For anything beyond API 36, we don't have enough information
        // Return -1 to indicate unknown
        return -1;
    }
    
    /**
     * Gets information about DEX version support.
     * 
     * @param dexVersion The DEX version number
     * @return A string describing the DEX version and its Android compatibility
     */
    public static String getDexVersionInfo(int dexVersion) {
        switch (dexVersion) {
            case 35:
                return "DEX v35 (Android 6.0 Marshmallow / API 23 and below)";
            case 37:
                return "DEX v37 (Android 7.0-7.1 Nougat / API 24-25)";
            case 38:
                return "DEX v38 (Android 8.0-8.1 Oreo / API 26-27)";
            case 39:
                return "DEX v39 (Android 9-10 / API 28-29)";
            case 40:
                return "DEX v40 (Android 11-14 / API 30-34)";
            case 41:
                return "DEX v41 (Android 15 / API 35)";
            case 42:
                return "DEX v42 (Android 16 / API 36 - Expected, not yet officially supported)";
            default:
                if (dexVersion > 42) {
                    return "DEX v" + dexVersion + " (Future Android version - Not yet supported)";
                } else if (dexVersion > 0) {
                    return "DEX v" + dexVersion + " (Legacy version)";
                }
                return "Unknown DEX version";
        }
    }
    
    /**
     * Checks if a DEX version is officially supported by the current smali library.
     * 
     * @param dexVersion The DEX version to check
     * @return true if officially supported, false otherwise
     */
    public static boolean isDexVersionSupported(int dexVersion) {
        // Currently supported DEX versions: 35, 37, 38, 39, 40, 41
        return dexVersion >= 35 && dexVersion <= 41 && dexVersion != 36;
    }
    
    /**
     * Gets a fallback DEX version for unsupported versions.
     * This allows the tool to process APKs with newer DEX versions
     * using the most recent supported version.
     * 
     * @param dexVersion The requested DEX version
     * @return A supported DEX version to use as fallback
     */
    public static int getFallbackDexVersion(int dexVersion) {
        if (isDexVersionSupported(dexVersion)) {
            return dexVersion;
        }
        
        // For any unsupported DEX version, fall back to v41 (latest supported)
        // This provides best-effort compatibility
        return 41;
    }
}
