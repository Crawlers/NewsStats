package com.cse10.duplicateDetector;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * this class is used to create 32bits and 64bits hash values for given string
 * this is murmur hash function. it non-cryptographic hash function
 * Created by chamath on 1/2/2015.
 */
public class HashCalculator {

    /**
     * calculate 32 bit hash value for given word
     * @param word
     * @return
     */
    public int hash32(String word) {
        //Each character is encoded as a 8 bit unit. Each byte of the buffer represents a character.
        //buffer length is equal to the length of the word
        byte[] buffer = word.getBytes(Charset.forName("utf-8"));
        System.out.println(buffer.length);
        return hash32(buffer, buffer.length, 0);
    }

    /**
     * calculate 64 bit hash value for given word
     * @param doc
     * @return
     */
    public static long hash64(String doc) {
        //Each character is encoded as a 8 bit unit. Each byte of the buffer represents a character.
        //buffer length is equal to the length of the word
        byte[] buffer = doc.getBytes(Charset.forName("utf-8"));
        return hash64(buffer, buffer.length, 0);
    }


    /**
     * implemented by Viliam Holub
     * https://github.com/tnm/murmurhash-java/blob/master/src/main/java/ie/ucd/murmur/MurmurHash.java
     * generate 32 bit hash from given byte buffer
     * @param data
     * @param length
     * @param seed
     * @return
     */
    public int hash32(byte[] data, int length, int seed) {
        // 'm' and 'r' are mixing constants generated offline.
        // They're not really 'magic', they just happen to work well.
        final int m = 0x5bd1e995;
        final int r = 24;

        // Initialize the hash to a random value
        int h = seed^length;
        int length4 = length/4;

        for (int i=0; i<length4; i++) {
            final int i4 = i*4;
            int k = (data[i4+0]&0xff) +((data[i4+1]&0xff)<<8)
                    +((data[i4+2]&0xff)<<16) +((data[i4+3]&0xff)<<24);
            k *= m;
            k ^= k >>> r;
            k *= m;
            h *= m;
            h ^= k;
        }

        // Handle the last few bytes of the input array
        switch (length%4) {
            case 3: h ^= (data[(length&~3) +2]&0xff) << 16;
            case 2: h ^= (data[(length&~3) +1]&0xff) << 8;
            case 1: h ^= (data[length&~3]&0xff);
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

    /**
     * implemented by Viliam Holub
     * https://github.com/tnm/murmurhash-java/blob/master/src/main/java/ie/ucd/murmur/MurmurHash.java
     * generate 64 bit hash from given byte buffer
     * @param data
     * @param length
     * @param seed
     * @return
     */
    public static long hash64(final byte[] data, int length, int seed) {
        final long m = 0xc6a4a7935bd1e995L;
        final int r = 47;

        long h = (seed&0xffffffffl)^(length*m);

        int length8 = length/8;

        for (int i=0; i<length8; i++) {
            final int i8 = i*8;
            long k =  ((long)data[i8+0]&0xff)      +(((long)data[i8+1]&0xff)<<8)
                    +(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
                    +(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
                    +(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        switch (length%8) {
            case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
            case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
            case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
            case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
            case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
            case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
            case 1: h ^= (long)(data[length&~7]&0xff);
                h *= m;
        };

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }

    public static void main(String[] args) {
       String word="apple";
       byte[] buffer = word.getBytes(Charset.forName("utf-8"));
       ByteBuffer data = ByteBuffer.wrap(buffer);
        int length=20;
        int answer=length>>2;
        System.out.println(answer);

        HashCalculator hashCalculator=new HashCalculator();
        System.out.println(Integer.toBinaryString(hashCalculator.hash32("apple")));
        System.out.println(Integer.toBinaryString(hashCalculator.hash32("applee")));
        System.out.println(Long.toBinaryString(hashCalculator.hash64("apple")));
        System.out.println(Long.toBinaryString(hashCalculator.hash64("applee")));
    }
    //TODO format the code

}
