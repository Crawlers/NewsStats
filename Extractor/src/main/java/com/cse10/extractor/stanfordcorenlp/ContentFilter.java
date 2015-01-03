package com.cse10.extractor.stanfordcorenlp;

/**
 * Created by TharinduWijewardane on 2015-01-02.
 */
public class ContentFilter {

    public static String manuelFilter(String text) {

        text = text.replaceAll(" Rs. ", " Rs ");

        return text;
    }

}
