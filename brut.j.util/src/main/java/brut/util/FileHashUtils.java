/*
 *  Copyright (C) 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 *  Copyright (C) 2010 Connor Tumbleson <connor.tumbleson@gmail.com>
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
package brut.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for computing file hashes and checksums.
 * Provides methods for computing MD5 and SHA-256 hashes of files,
 * which is useful for validating APK file integrity.
 */
public final class FileHashUtils {
    private static final int BUFFER_SIZE = 8192;
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private FileHashUtils() {
        // Private constructor for utility class.
    }

    /**
     * Computes the MD5 hash of a file.
     *
     * @param file the file to hash
     * @return the MD5 hash as a hexadecimal string
     * @throws IOException if an I/O error occurs reading the file
     * @throws IllegalArgumentException if the file is null or does not exist
     */
    public static String computeMD5(File file) throws IOException {
        return computeHash(file, "MD5");
    }

    /**
     * Computes the SHA-256 hash of a file.
     *
     * @param file the file to hash
     * @return the SHA-256 hash as a hexadecimal string
     * @throws IOException if an I/O error occurs reading the file
     * @throws IllegalArgumentException if the file is null or does not exist
     */
    public static String computeSHA256(File file) throws IOException {
        return computeHash(file, "SHA-256");
    }

    /**
     * Computes the hash of a file using the specified algorithm.
     *
     * @param file the file to hash
     * @param algorithm the hash algorithm (e.g., "MD5", "SHA-256")
     * @return the hash as a hexadecimal string
     * @throws IOException if an I/O error occurs reading the file
     * @throws IllegalArgumentException if the file is null or does not exist
     */
    public static String computeHash(File file, String algorithm) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("Not a file: " + file.getAbsolutePath());
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            try (InputStream input = new FileInputStream(file)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Hash algorithm not available: " + algorithm, e);
        }
    }

    /**
     * Verifies that a file matches the expected MD5 hash.
     *
     * @param file the file to verify
     * @param expectedHash the expected MD5 hash as a hexadecimal string
     * @return true if the file's MD5 hash matches the expected hash, false otherwise
     * @throws IOException if an I/O error occurs reading the file
     * @throws IllegalArgumentException if the file is null or does not exist
     */
    public static boolean verifyMD5(File file, String expectedHash) throws IOException {
        if (expectedHash == null) {
            throw new IllegalArgumentException("Expected hash cannot be null");
        }
        String actualHash = computeMD5(file);
        return actualHash.equalsIgnoreCase(expectedHash);
    }

    /**
     * Verifies that a file matches the expected SHA-256 hash.
     *
     * @param file the file to verify
     * @param expectedHash the expected SHA-256 hash as a hexadecimal string
     * @return true if the file's SHA-256 hash matches the expected hash, false otherwise
     * @throws IOException if an I/O error occurs reading the file
     * @throws IllegalArgumentException if the file is null or does not exist
     */
    public static boolean verifySHA256(File file, String expectedHash) throws IOException {
        if (expectedHash == null) {
            throw new IllegalArgumentException("Expected hash cannot be null");
        }
        String actualHash = computeSHA256(file);
        return actualHash.equalsIgnoreCase(expectedHash);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes the byte array to convert
     * @return the hexadecimal string representation
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
