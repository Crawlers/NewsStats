package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class LocationDetector {

    private static TokenSequencePattern caughtPattern = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|remanded|raided/ | /took/ /into/ /custody/ | /taken/ /into/ /custody/ ) []{0,15} [{tag:IN}]{1,2} []{0,3} ([ner: LOCATION])");

    public static String findLocation(List<CoreLabel> tokens) {

        String location = "";

        TokenSequenceMatcher caughtMatcher = caughtPattern.getMatcher(tokens);
        while (caughtMatcher.find()) {
            location = caughtMatcher.group(caughtMatcher.groupCount()); // get last group
            System.out.println(" caught location: " + location);
        }

        return location;

    }

}
