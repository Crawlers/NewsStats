package com.cse10.duplicateDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamath on 1/2/2015.
 */
public class BinoryWordSegmenter extends WordSegmenter {

    private StringBuilder stringBuilder;

    public BinoryWordSegmenter() {
        stringBuilder = new StringBuilder();
    }

    @Override
    protected List<String> getWords(String document) {
        List<String> binaryWords = new ArrayList<String>();
        for (int i = 0; i < document.length() - 1; i += 1) {
            stringBuilder.append(document.charAt(i)).append(document.charAt(i + 1));
            binaryWords.add(stringBuilder.toString());
        }
        return binaryWords;
    }
}