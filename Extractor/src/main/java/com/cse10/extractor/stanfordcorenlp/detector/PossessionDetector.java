package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class PossessionDetector {

    private static TokenSequencePattern crimePossessionPattern = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|raided/ | /taken/ /into/ /custody/ ) []{0,6} /with/ [{tag:CD}]{0,2} ([{tag:NN}]{0,3} [{tag:NNS}]{0,3})");
    private static TokenSequencePattern crimePossessionPattern2 = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|raided/ | /taken/ /into/ /custody/ ) []{0,6} /for|in/ /possession/ /of/ [{tag:CD}]{0,2} ([{tag:NN}]{0,3} [{tag:NNS}]{0,3})");
    private static TokenSequencePattern crimePossessionPattern3 = TokenSequencePattern.compile("[{tag:CD}]{0,2} []{0,4} ([{tag:JJ}]{0,2} [{tag:NNS}]{1,3}) []{0,4} /seized|captured/ []{0,2} /in|from/ []{0,3} /possession/");

    public static String findPossession(List<CoreLabel> tokens) {

        String possession = "";

        TokenSequenceMatcher possessionMatcher = crimePossessionPattern.getMatcher(tokens);
        while (possessionMatcher.find()) {
            possession = possessionMatcher.group(possessionMatcher.groupCount()); // get last group
            System.out.println(" possession: " + possession);
        }

        if (possession.length() > 0) {
            return possession;
        }

        possessionMatcher = crimePossessionPattern2.getMatcher(tokens);
        while (possessionMatcher.find()) {
            possession = possessionMatcher.group(possessionMatcher.groupCount()); // get last group
            System.out.println(" possession: " + possession);
        }

        if (possession.length() > 0) {
            return possession;
        }

        possessionMatcher = crimePossessionPattern3.getMatcher(tokens);
        while (possessionMatcher.find()) {
            possession = possessionMatcher.group(possessionMatcher.groupCount()); // get last group
            System.out.println(" possession: " + possession);
        }

        return possession;
    }

}
