package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class CourtDetector {

    private static TokenSequencePattern courtPattern = TokenSequencePattern.compile("([{tag:NNP}]{1,3}) /Magistrate?/ []{0,2} /Court/");

    public static String findCourt(List<CoreLabel> tokens) {

        String court = "";

        TokenSequenceMatcher courtMatcher = courtPattern.getMatcher(tokens);
        while (courtMatcher.find()) {
            court = courtMatcher.group(1);
            System.out.println(" court: " + court);
        }

        return court;
    }

}
