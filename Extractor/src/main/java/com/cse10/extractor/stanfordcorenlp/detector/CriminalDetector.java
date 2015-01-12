package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class CriminalDetector {

    private static TokenSequencePattern criminalPattern = TokenSequencePattern.compile("(/killed|murdered|stabbed|raped/) []{0,4} /by/ []{0,2} ([ner: PERSON]{1,4})");
    private static TokenSequencePattern criminalActivePattern = TokenSequencePattern.compile("([ner: PERSON]{1,4}) []{0,4} (/killed|murdered|stabbed|raped/)");

    public static String findCriminal(List<CoreLabel> tokens) {

        String criminal = "";

        TokenSequenceMatcher criminalMatcher = criminalPattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            criminal = criminalMatcher.group(criminalMatcher.groupCount()); // get last group
            System.out.println(" criminal: " + criminal);
        }

        if (criminal.length() > 0) {
            return criminal;
        }

        criminalMatcher = criminalActivePattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            criminal = criminalMatcher.group(1);
            System.out.println(" criminal: " + criminal);
        }

        return criminal;

    }

}
