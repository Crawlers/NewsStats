package com.cse10.duplicateDetector;

/**
 * Created by Chamath on 2/4/2015.
 */
public class HammingDistanceCalculator {

    /**
     * calculate hamming distance for two integers
     * Hamming distance between two strings of equal length is the number of positions at which the corresponding
     * symbols are different
     * @param hash1
     * @param hash2
     * @return
     */
    public int getHammingDistance(int hash1, int hash2) {
        int i = hash1 ^ hash2;
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        i = (i + (i >>> 4)) & 0x0f0f0f0f;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        return i & 0x3f;
    }

    /**
     * calculate hamming distance for two long values
     * @param hash1
     * @param hash2
     * @return
     */
    public int getHammingDistance(long hash1, long hash2) {
        long i = hash1 ^ hash2;
        i = i - ((i >>> 1) & 0x5555555555555555L);
        i = (i & 0x3333333333333333L) + ((i >>> 2) & 0x3333333333333333L);
        i = (i + (i >>> 4)) & 0x0f0f0f0f0f0f0f0fL;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        i = i + (i >>> 32);
        return (int) i & 0x7f;
    }

}
