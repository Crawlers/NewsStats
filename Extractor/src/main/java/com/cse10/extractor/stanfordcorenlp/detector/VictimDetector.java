package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class VictimDetector {

    private static TokenSequencePattern victimPattern = TokenSequencePattern.compile("([{tag:NNS}]{0,3} [{tag:NN}]{0,3} [{tag:NNP}]{0,3}) [{tag:VBD}]{1,2} (/killed|murdered|stabbed|raped/)");
    private static TokenSequencePattern suicidePattern = TokenSequencePattern.compile("([{tag:NNS}]{0,3} [{tag:NN}]{0,3} [{tag:NNP}]{0,3}) /committed/ /suicide/");

    public static String findVictim(List<CoreLabel> tokens) {

        String victim = "";

        TokenSequenceMatcher victimMatcher = victimPattern.getMatcher(tokens);
        while (victimMatcher.find()) {
            victim = victimMatcher.group(1);
            System.out.println(" victim: " + victim);
        }

        if (victim.length() > 0) {
            return victim;
        }

        TokenSequenceMatcher suicideVictimMatcher = suicidePattern.getMatcher(tokens);
        while (suicideVictimMatcher.find()) {
            victim = suicideVictimMatcher.group(1);
            System.out.println(" victim: " + victim);
        }

        return victim;
    }

}
