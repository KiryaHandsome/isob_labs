package by.bsuir.des;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncryptionUtils {

    private static final int[] PC1 =
            {
                    57, 49, 41, 33, 25, 17, 9,
                    1, 58, 50, 42, 34, 26, 18,
                    10, 2, 59, 51, 43, 35, 27,
                    19, 11, 3, 60, 52, 44, 36,
                    63, 55, 47, 39, 31, 23, 15,
                    7, 62, 54, 46, 38, 30, 22,
                    14, 6, 61, 53, 45, 37, 29,
                    21, 13, 5, 28, 20, 12, 4
            };

    // First index is garbage value, loops operating on this should start with index = 1
    private static final int[] KEY_SHIFTS = {
            0, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };


    private static final int[][] s1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    private static final int[][] s2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
    };

    private static final int[][] s3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
    };

    private static final int[][] s4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
    };

    private static final int[][] s5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
    };

    private static final int[][] s6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
    };

    private static final int[][] s7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
    };

    private static final int[][] s8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    private static final int[][][] s = {s1, s2, s3, s4, s5, s6, s7, s8};

    private static final int[] g = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };


    static int[] p = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    static int[] IPi = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final long[] K = new long[17];

    private static String binToHex(String bin) {
        BigInteger b = new BigInteger(bin, 2);
        return b.toString(16);
    }

    private static String hexToBin(String hex) {
        BigInteger b = new BigInteger(hex, 16);
        return b.toString(2);
    }

    private static String binToUTF(String bin) {
        byte[] ciphertextBytes = new byte[bin.length() / 8];
        String ciphertext;
        for (int j = 0; j < ciphertextBytes.length; j++) {
            String temp = bin.substring(0, 8);
            byte b = (byte) Integer.parseInt(temp, 2);
            ciphertextBytes[j] = b;
            bin = bin.substring(8);
        }

        ciphertext = new String(ciphertextBytes, StandardCharsets.UTF_8);
        return ciphertext.trim();
    }

    private static String utfToBin(String utf) {
        byte[] bytes;
        bytes = utf.getBytes(StandardCharsets.UTF_8);

        StringBuilder bin = new StringBuilder();
        for (int current : bytes) {
            int value = current;
            for (int j = 0; j < 8; j++) {
                bin.append((value & 128) == 0 ? 0 : 1);
                value <<= 1;
            }
        }
        return bin.toString();
    }

    public static byte[] encrypt(byte[] data, String key) {
        String plaintext = utfToBin(new String(data));

        buildKeySchedule(hash(key));

        StringBuilder binPlaintext = new StringBuilder(plaintext);

        // Add padding if necessary
        int remainder = binPlaintext.length() % 64;
        if (remainder != 0) {
            for (int i = 0; i < (64 - remainder); i++)
                binPlaintext.insert(0, "0");
        }

        // Separate binary plaintext into blocks
        String[] binPlaintextBlocks = new String[binPlaintext.length() / 64];
        int offset = 0;
        for (int i = 0; i < binPlaintextBlocks.length; i++) {
            binPlaintextBlocks[i] = binPlaintext.substring(offset, offset + 64);
            offset += 64;
        }

        String[] binCiphertextBlocks = new String[binPlaintext.length() / 64];

        // Encrypt the blocks
        for (int i = 0; i < binCiphertextBlocks.length; i++)
            try {
                binCiphertextBlocks[i] = encryptBlock(binPlaintextBlocks[i]);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }

        // Build the ciphertext binary string from the blocks
        StringBuilder binCiphertext = new StringBuilder();
        for (int i = 0; i < binCiphertextBlocks.length; i++)
            binCiphertext.append(binCiphertextBlocks[i]);

        // Destroy key schedule
        Arrays.fill(K, 0);

        return binToHex(binCiphertext.toString()).getBytes();
    }

    public static String decrypt(byte[] data, String key) {
        String plaintext = hexToBin(new String(data));

        buildKeySchedule(hash(key));

        StringBuilder binPlaintext;

        binPlaintext = new StringBuilder(plaintext);

        // Add padding if necessary
        int remainder = binPlaintext.length() % 64;
        if (remainder != 0) {
            for (int i = 0; i < (64 - remainder); i++)
                binPlaintext.insert(0, "0");
        }

        // Separate binary plaintext into blocks
        String[] binPlaintextBlocks = new String[binPlaintext.length() / 64];
        int offset = 0;
        for (int i = 0; i < binPlaintextBlocks.length; i++) {
            binPlaintextBlocks[i] = binPlaintext.substring(offset, offset + 64);
            offset += 64;
        }

        String[] binCiphertextBlocks = new String[binPlaintext.length() / 64];

        // Encrypt the blocks
        for (int i = 0; i < binCiphertextBlocks.length; i++) {
            try {
                binCiphertextBlocks[i] = decryptBlock(binPlaintextBlocks[i]);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        // Build the ciphertext binary string from the blocks
        StringBuilder binCiphertext = new StringBuilder();
        for (String binCiphertextBlock : binCiphertextBlocks)
            binCiphertext.append(binCiphertextBlock);

        // Destroy key schedule
        Arrays.fill(K, 0);

        return binToUTF(binCiphertext.toString());
    }

    public static String encryptBlock(String plaintextBlock) throws Exception {
        int length = plaintextBlock.length();
        if (length != 64)
            throw new RuntimeException("Input block length is not 64 bits!");

        //Initial permutation
        StringBuilder out = new StringBuilder();
        for (int j : IP) {
            out.append(plaintextBlock.charAt(j - 1));
        }

        String mL = out.substring(0, 32);
        String mR = out.substring(32);

        for (int i = 0; i < 16; i++) {

            // 48-bit current key
            StringBuilder curKey = new StringBuilder(Long.toBinaryString(K[i + 1]));
            while (curKey.length() < 48)
                curKey.insert(0, "0");

            // Get 32-bit result from f with m1 and ki
            String fResult = f(mR, curKey.toString());

            // XOR m0 and f
            long f = Long.parseLong(fResult, 2);
            long cmL = Long.parseLong(mL, 2);

            long m2 = cmL ^ f;
            StringBuilder m2String = new StringBuilder(Long.toBinaryString(m2));

            while (m2String.length() < 32)
                m2String.insert(0, "0");

            mL = mR;
            mR = m2String.toString();
        }

        String in = mR + mL;
        StringBuilder output = new StringBuilder();
        for (int j : IPi) {
            output.append(in.charAt(j - 1));
        }

        return output.toString();
    }

    public static String decryptBlock(String plaintextBlock) throws RuntimeException {
        int length = plaintextBlock.length();
        if (length != 64)
            throw new RuntimeException("Input block length is not 64 bits!");

        //Initial permutation
        StringBuilder out = new StringBuilder();
        for (int j : IP) {
            out.append(plaintextBlock.charAt(j - 1));
        }

        String mL = out.substring(0, 32);
        String mR = out.substring(32);

        for (int i = 16; i > 0; i--) {

            // 48-bit current key
            StringBuilder curKey = new StringBuilder(Long.toBinaryString(K[i]));
            while (curKey.length() < 48)
                curKey.insert(0, "0");

            // Get 32-bit result from f with m1 and ki
            String fResult = f(mR, curKey.toString());

            // XOR m0 and f
            long f = Long.parseLong(fResult, 2);
            long cmL = Long.parseLong(mL, 2);

            long m2 = cmL ^ f;
            StringBuilder m2String = new StringBuilder(Long.toBinaryString(m2));

            while (m2String.length() < 32)
                m2String.insert(0, "0");

            mL = mR;
            mR = m2String.toString();
        }

        String in = mR + mL;
        StringBuilder output = new StringBuilder();
        for (int j : IPi) {
            output.append(in.charAt(j - 1));
        }

        return output.toString();
    }

    /**
     <a href="http://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings">...</a>
     */
    public static long hash(String string) {
        long h = 1125899906842597L; // prime
        int len = string.length();

        for (int i = 0; i < len; i++) {
            h = 31 * h + string.charAt(i);
        }
        return h;
    }

    public static void buildKeySchedule(long key) {
        // Convert long value to 64bit binary string
        StringBuilder binKey = new StringBuilder(Long.toBinaryString(key));

        // Add leading zeros if not at key length for ease of computations
        while (binKey.length() < 64)
            binKey.insert(0, "0");

        // For the 56-bit permuted key
        StringBuilder binKey_PC1 = new StringBuilder();

        // Apply Permuted Choice 1 (64 -> 56 bit)
        for (int k : PC1)
            binKey_PC1.append(binKey.charAt(k - 1));

        String sL, sR;
        int iL, iR;

        // Split permuted string in half | 56/2 = 28
        sL = binKey_PC1.substring(0, 28);
        sR = binKey_PC1.substring(28);

        // Parse binary strings into integers for shifting
        iL = Integer.parseInt(sL, 2);
        iR = Integer.parseInt(sR, 2);

        // Build the keys (Start at index 1)
        for (int i = 1; i < K.length; i++) {

            // Perform left shifts according to key shift array
            iL = Integer.rotateLeft(iL, KEY_SHIFTS[i]);
            iR = Integer.rotateLeft(iR, KEY_SHIFTS[i]);

            StringBuilder binKey_PC2 = mergeTwoHalves(iL, iR);

            // Set the 48-bit key
            K[i] = Long.parseLong(binKey_PC2.toString(), 2);
        }
    }

    private static StringBuilder mergeTwoHalves(long iL, int iR) {
        long merged = (iL << 28) + iR;

        StringBuilder sMerged = new StringBuilder(Long.toBinaryString(merged));

        // Fix length if leading zeros absent
        while (sMerged.length() < 56)
            sMerged.insert(0, "0");

        StringBuilder binKey_PC2 = new StringBuilder();

        // Apply Permuted Choice 2 (56 -> 48 bit)
        for (int k : PC2)
            binKey_PC2.append(sMerged.charAt(k - 1));
        return binKey_PC2;
    }

    public static String f(String mi, String key) {
        // Expansion function g (named E in fips pub 46)
        StringBuilder gMi = new StringBuilder();
        for (int value : g) {
            gMi.append(mi.charAt(value - 1));
        }

        long m = Long.parseLong(gMi.toString(), 2);
        long k = Long.parseLong(key, 2);

        // XOR expanded message block and key block (48 bits)
        long result = m ^ k;

        StringBuilder bin = new StringBuilder(Long.toBinaryString(result));
        // Making sure the string is 48 bits
        while (bin.length() < 48) {
            bin.insert(0, "0");
        }

        // Split into eight 6-bit strings
        String[] sin = new String[8];
        for (int i = 0; i < 8; i++) {
            sin[i] = bin.substring(0, 6);
            bin = new StringBuilder(bin.substring(6));
        }

        // Do S-Box calculations
        String[] output = new String[8];
        for (int i = 0; i < 8; i++) {
            int[][] curS = s[i];
            String cur = sin[i];

            // Get binary values
            int row = Integer.parseInt(cur.charAt(0) + "" + cur.charAt(5), 2);
            int col = Integer.parseInt(cur.substring(1, 5), 2);

            // Do S-Box table lookup
            output[i] = Integer.toBinaryString(curS[row][col]);

            // Make sure the string is 4 bits
            while (output[i].length() < 4)
                output[i] = "0" + output[i];

        }

        // Merge S-Box outputs into one 32-bit string
        StringBuilder merged = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            merged.append(output[i]);
        }

        // Apply Permutation P
        StringBuilder mergedP = new StringBuilder();
        for (int j : p) {
            mergedP.append(merged.charAt(j - 1));
        }

        return mergedP.toString();
    }
}