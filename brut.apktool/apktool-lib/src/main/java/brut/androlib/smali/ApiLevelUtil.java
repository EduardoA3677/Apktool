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

import brut.common.Log;

/**
 * Utility class for handling Android API levels and ensuring forward compatibility
 * with future Android versions.
 */
public class ApiLevelUtil {
    private static final String TAG = ApiLevelUtil.class.getName();
    
    // Maximum API level currently supported by smali/baksmali library
    private static final int MAX_SUPPORTED_API = 35; // Android 15
    
    // Expected future API levels (for informational purposes)
    private static final int API_ANDROID_16 = 36;
    
    /**
     * Validates and normalizes an API level for use with smali/baksmali.
     * 
     * @param apiLevel The requested API level
     * @return A validated API level that is supported by the smali library
     */
    public static int getValidatedApiLevel(int apiLevel) {
        if (apiLevel <= 0) {
            Log.w(TAG, "Invalid API level: " + apiLevel + ", using default");
            return MAX_SUPPORTED_API;
        }
        
        if (apiLevel > MAX_SUPPORTED_API) {
            Log.w(TAG, String.format(
                "API level %d is not yet supported by smali library (max: %d). " +
                "Using maximum supported API level. " +
                "APK functionality may be limited until smali library is updated.",
                apiLevel, MAX_SUPPORTED_API));
            return MAX_SUPPORTED_API;
        }
        
        return apiLevel;
    }
    
    /**
     * Checks if the given API level is supported by the current smali library.
     * 
     * @param apiLevel The API level to check
     * @return true if supported, false otherwise
     */
    public static boolean isApiLevelSupported(int apiLevel) {
        return apiLevel > 0 && apiLevel <= MAX_SUPPORTED_API;
    }
    
    /**
     * Gets the maximum supported API level.
     * 
     * @return The maximum API level supported by the smali library
     */
    public static int getMaxSupportedApiLevel() {
        return MAX_SUPPORTED_API;
    }
    
    /**
     * Gets a human-readable Android version name for an API level.
     * 
     * @param apiLevel The API level
     * @return The Android version name
     */
    public static String getAndroidVersionName(int apiLevel) {
        switch (apiLevel) {
            case 35: return "Android 15 (Vanilla Ice Cream)";
            case 34: return "Android 14 (Upside Down Cake)";
            case 33: return "Android 13 (Tiramisu)";
            case 32: return "Android 12L";
            case 31: return "Android 12";
            case 30: return "Android 11";
            case 29: return "Android 10 (Q)";
            case 28: return "Android 9 (Pie)";
            case 27: return "Android 8.1 (Oreo)";
            case 26: return "Android 8.0 (Oreo)";
            case 25: return "Android 7.1 (Nougat)";
            case 24: return "Android 7.0 (Nougat)";
            case 23: return "Android 6.0 (Marshmallow)";
            case API_ANDROID_16: return "Android 16 (expected)";
            default:
                if (apiLevel > MAX_SUPPORTED_API) {
                    return "Future Android version (API " + apiLevel + ")";
                }
                return "Android API " + apiLevel;
        }
    }
    
    /**
     * Logs information about API level support when processing an APK.
     * 
     * @param requestedApi The API level requested/detected from the APK
     * @param effectiveApi The actual API level that will be used
     */
    public static void logApiLevelInfo(int requestedApi, int effectiveApi) {
        if (requestedApi != effectiveApi) {
            Log.i(TAG, String.format(
                "APK targets %s (API %d), using %s (API %d) for processing",
                getAndroidVersionName(requestedApi), requestedApi,
                getAndroidVersionName(effectiveApi), effectiveApi));
        } else {
            Log.i(TAG, String.format(
                "Processing APK with %s (API %d)",
                getAndroidVersionName(effectiveApi), effectiveApi));
        }
    }
}
