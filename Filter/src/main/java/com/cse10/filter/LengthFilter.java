package com.cse10.filter;

/**
 * Created by Tharindu on 2014-11-06.
 */
public class LengthFilter {

    public static boolean filterContent(String content) {

        if (content.length() > 10) {
            return true;
        } else {
            return false;
        }

    }

}
