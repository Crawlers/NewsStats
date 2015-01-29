package com.cse10.duplicateDetector;

import java.util.List;

/**
 * Created by chamath on 1/2/2015.
 */
public class SimHashCalculator {
    private WordSegmenter wordSeg;
    private HashCalculator hashCalculator;


    public SimHashCalculator(WordSegmenter wordSeg) {
        this.wordSeg = wordSeg;
        hashCalculator=new HashCalculator();
    }

    /**
     * calculate hamming distance for two integers
     * Hamming distance between two strings of equal length is the number of positions at which the corresponding
     * symbols are different
     * @param hash1
     * @param hash2
     * @return
     */
    public int hammingDistance(int hash1, int hash2) {
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
    public int hammingDistance(long hash1, long hash2) {
        long i = hash1 ^ hash2;
        i = i - ((i >>> 1) & 0x5555555555555555L);
        i = (i & 0x3333333333333333L) + ((i >>> 2) & 0x3333333333333333L);
        i = (i + (i >>> 4)) & 0x0f0f0f0f0f0f0f0fL;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        i = i + (i >>> 32);
        return (int) i & 0x7f;
    }

    /**
     * calculate signature/finger print value (64 bit) for the given document
     * @param document
     * @return
     */
    public long simhash64(String document)  {
        int bitLen = 64;
        int[] bits = new int[bitLen];
        List<String> tokens = wordSeg.getWords(document);

        //for each token in string
        for (String word : tokens) {

            //calculate 64 bit hash value for each token
            word = word.toLowerCase();
            long hashValue = hashCalculator.getHash64Value(word);
            for (int i = bitLen; i >= 1; --i) {
                if (((hashValue >> (bitLen - i)) & 1) == 1) {
                    ++bits[i - 1];
                } else {
                    --bits[i - 1];
                }
            }
        }
        long hash = 0x0000000000000000;
        long one = 0x0000000000000001;
        for (int i = bitLen; i >= 1; --i) {
            if (bits[i - 1] > 1) {
                hash |= one;
            }
            one = one << 1;
        }
        return hash;
    }

    /**
     * calculate signature/finger print value (32 bit) for the given document
     * @param doc
     * @return
     */
    public long simhash32(String doc) {
        int bitLen = 32;
        int[] bits = new int[bitLen];
        List<String> tokens = wordSeg.getWords(doc);
        for (String t : tokens) {
            int v = hashCalculator.getHash32Value(t);
            for (int i = bitLen; i >= 1; --i) {
                if (((v >> (bitLen - i)) & 1) == 1) {
                    ++bits[i - 1];
                } else {
                    --bits[i - 1];
                }
            }
        }
        int hash = 0x00000000;
        int one = 0x00000001;
        for (int i = bitLen; i >= 1; --i) {
            if (bits[i - 1] > 1) {
                hash |= one;
            }
            one = one << 1;
        }
        return hash;
    }

    public static void main(String[] args) {
        SimHashCalculator simHashCalculator=new SimHashCalculator(new FullWordSegmenter());
        long hash1=simHashCalculator.simhash64("child sex crime 2012-11-08 Anuradhapura Anuradhapura Anuradhapura Anuradhapura");
        long hash2=simHashCalculator.simhash64("illegal trade 2012 12 26 Anuradhapura Anuradhapura Anuradhapura Anuradhapura");
        System.out.println(Long.toBinaryString(hash1));
        System.out.println(Long.toBinaryString(hash2));
        System.out.println(simHashCalculator.hammingDistance(hash1,hash2));
    }

}
