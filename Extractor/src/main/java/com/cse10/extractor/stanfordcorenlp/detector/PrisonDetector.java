package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class PrisonDetector {

    protected static TokenSequencePattern prisonPattern = TokenSequencePattern.compile("/escaped/ /from/ ([ner: LOCATION]) /prison/");

    public static String findPrison(List<CoreLabel> tokens) {

        String prison = "";

        TokenSequenceMatcher prisonMatcher = prisonPattern.getMatcher(tokens);
        while (prisonMatcher.find()) {
            prison = prisonMatcher.group(prisonMatcher.groupCount()); // get last group
            System.out.println(" prison: " + prison);
        }

        return prison;
    }

}
