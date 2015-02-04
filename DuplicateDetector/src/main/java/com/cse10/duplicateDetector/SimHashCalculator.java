package com.cse10.duplicateDetector;

import java.util.List;

/**
 * Created by Chamath on 1/2/2015.
 */
public class SimHashCalculator {
    private WordSegmenter wordSegmenter;
    private HashCalculator hashCalculator;


    public SimHashCalculator(WordSegmenter wordSegmenter) {
        this.wordSegmenter = wordSegmenter;
        hashCalculator=new HashCalculator();
    }


    /**
     * calculate signature/finger print value (64 bit) for the given document
     * @param document
     * @return
     */
    public long getSimhash64Value(String document)  {
        int bitLen = 64;
        int[] bits = new int[bitLen];
        List<String> tokens = wordSegmenter.getWords(document);

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
    public long getSimhash32Value(String doc) {
        int bitLen = 32;
        int[] bits = new int[bitLen];
        List<String> tokens = wordSegmenter.getWords(doc);
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
        long hash1=simHashCalculator.getSimhash64Value("child sex crime 2012-11-08 Anuradhapura Anuradhapura Anuradhapura Anuradhapura");
        long hash2=simHashCalculator.getSimhash64Value("illegal trade 2012 12 26 Anuradhapura Anuradhapura Anuradhapura Anuradhapura");
        System.out.println(Long.toBinaryString(hash1));
        System.out.println(Long.toBinaryString(hash2));
    }

}
