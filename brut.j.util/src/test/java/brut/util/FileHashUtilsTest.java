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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for FileHashUtils.
 */
public class FileHashUtilsTest {
    private File tempFile;
    private File tempDir;

    @Before
    public void setUp() throws IOException {
        tempDir = new File(System.getProperty("java.io.tmpdir"), "apktool-test-" + System.currentTimeMillis());
        tempDir.mkdirs();
        tempFile = new File(tempDir, "test-file.txt");
    }

    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
        if (tempDir != null && tempDir.exists()) {
            tempDir.delete();
        }
    }

    @Test
    public void testComputeMD5() throws IOException {
        // Create a test file with known content
        writeToFile(tempFile, "Hello World");

        // Expected MD5 hash for "Hello World"
        String expectedMD5 = "b10a8db164e0754105b7a99be72e3fe5";

        String actualMD5 = FileHashUtils.computeMD5(tempFile);
        assertEquals(expectedMD5, actualMD5);
    }

    @Test
    public void testComputeSHA256() throws IOException {
        // Create a test file with known content
        writeToFile(tempFile, "Hello World");

        // Expected SHA-256 hash for "Hello World"
        String expectedSHA256 = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";

        String actualSHA256 = FileHashUtils.computeSHA256(tempFile);
        assertEquals(expectedSHA256, actualSHA256);
    }

    @Test
    public void testComputeMD5EmptyFile() throws IOException {
        // Create an empty file
        tempFile.createNewFile();

        // Expected MD5 hash for empty file
        String expectedMD5 = "d41d8cd98f00b204e9800998ecf8427e";

        String actualMD5 = FileHashUtils.computeMD5(tempFile);
        assertEquals(expectedMD5, actualMD5);
    }

    @Test
    public void testComputeSHA256EmptyFile() throws IOException {
        // Create an empty file
        tempFile.createNewFile();

        // Expected SHA-256 hash for empty file
        String expectedSHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        String actualSHA256 = FileHashUtils.computeSHA256(tempFile);
        assertEquals(expectedSHA256, actualSHA256);
    }

    @Test
    public void testVerifyMD5Success() throws IOException {
        writeToFile(tempFile, "Test Content");
        String expectedHash = FileHashUtils.computeMD5(tempFile);

        assertTrue(FileHashUtils.verifyMD5(tempFile, expectedHash));
    }

    @Test
    public void testVerifyMD5Failure() throws IOException {
        writeToFile(tempFile, "Test Content");

        assertFalse(FileHashUtils.verifyMD5(tempFile, "wronghash123456"));
    }

    @Test
    public void testVerifySHA256Success() throws IOException {
        writeToFile(tempFile, "Test Content");
        String expectedHash = FileHashUtils.computeSHA256(tempFile);

        assertTrue(FileHashUtils.verifySHA256(tempFile, expectedHash));
    }

    @Test
    public void testVerifySHA256Failure() throws IOException {
        writeToFile(tempFile, "Test Content");

        assertFalse(FileHashUtils.verifySHA256(tempFile, "wronghash123456"));
    }

    @Test
    public void testHashCaseInsensitive() throws IOException {
        writeToFile(tempFile, "Test Content");
        String hash = FileHashUtils.computeMD5(tempFile);

        assertTrue(FileHashUtils.verifyMD5(tempFile, hash.toUpperCase()));
        assertTrue(FileHashUtils.verifyMD5(tempFile, hash.toLowerCase()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeMD5NullFile() throws IOException {
        FileHashUtils.computeMD5(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeSHA256NullFile() throws IOException {
        FileHashUtils.computeSHA256(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeMD5NonExistentFile() throws IOException {
        File nonExistent = new File(tempDir, "non-existent.txt");
        FileHashUtils.computeMD5(nonExistent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeMD5Directory() throws IOException {
        FileHashUtils.computeMD5(tempDir);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyMD5NullHash() throws IOException {
        tempFile.createNewFile();
        FileHashUtils.verifyMD5(tempFile, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifySHA256NullHash() throws IOException {
        tempFile.createNewFile();
        FileHashUtils.verifySHA256(tempFile, null);
    }

    @Test
    public void testComputeHashWithDifferentContent() throws IOException {
        writeToFile(tempFile, "Content A");
        String hashA = FileHashUtils.computeMD5(tempFile);

        writeToFile(tempFile, "Content B");
        String hashB = FileHashUtils.computeMD5(tempFile);

        assertNotEquals(hashA, hashB);
    }

    @Test
    public void testComputeHashLargeFile() throws IOException {
        // Create a larger file to test buffer handling
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeContent.append("This is line ").append(i).append("\n");
        }
        writeToFile(tempFile, largeContent.toString());

        // Just verify we can compute the hash without errors
        String md5 = FileHashUtils.computeMD5(tempFile);
        assertNotNull(md5);
        assertEquals(32, md5.length()); // MD5 is 32 hex characters

        String sha256 = FileHashUtils.computeSHA256(tempFile);
        assertNotNull(sha256);
        assertEquals(64, sha256.length()); // SHA-256 is 64 hex characters
    }

    @Test
    public void testComputeHashConsistency() throws IOException {
        writeToFile(tempFile, "Consistent Content");

        String hash1 = FileHashUtils.computeMD5(tempFile);
        String hash2 = FileHashUtils.computeMD5(tempFile);

        assertEquals(hash1, hash2);
    }

    private void writeToFile(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
