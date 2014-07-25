package com.cse10.filter;

/**
 * Created by TharinduWijewardane on 25.07.2014.
 */

/**
 * does the initial filtering to extract crime related articles
 */
public class KeywordsFilter {

    private static final String[] LEVEL_ONE_KEYWORDS = {"kill", "killed", "murder", "murderer", "theft", "stole", "steal", "stolen", "gone"};

    private static final String[] LEVEL_TWO_KEYWORDS = {"kill", "killed", "murder", "murderer", "theft"};

    public static boolean filterContent(String content) {

        if (filterLevel(LEVEL_ONE_KEYWORDS, content)) {
            if (filterLevel(LEVEL_TWO_KEYWORDS, content)) {
                return true;
            }
        }

        return false;
    }

    private static boolean filterLevel(String[] keywords, String content) {
        for (int i = 0; i < keywords.length; i++) {
            if (content.contains(" " + keywords[i] + " ")) { // checking for "<space><keyword><space>"
                return true;
            }
        }

        return false;
    }

}
