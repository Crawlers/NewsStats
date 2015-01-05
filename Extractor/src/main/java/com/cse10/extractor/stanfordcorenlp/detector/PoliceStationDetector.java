package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class PoliceStationDetector {

    protected static TokenSequencePattern policePattern = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|remanded|raided/ | /taken/ /into/ /custody/ ) []{0,8} /by/ [/the/]{0,1} ([{tag:NNP}]{1,3}) /police|Police/");
    protected static TokenSequencePattern policeActivePattern = TokenSequencePattern.compile("([{tag:NNP}]{1,3}) /police|Police/ []{0,4} (/arrested|apprehended|nabbed|seized|remanded|raided/ | /took/ /into/ /custody/ )");
    protected static TokenSequencePattern policeAllPattern = TokenSequencePattern.compile("([{tag:NNP}]{1,3}) /police|Police/");

    public static void printPolice(List<CoreLabel> tokens) {

        TokenSequenceMatcher policeMatcher = policePattern.getMatcher(tokens);
        while (policeMatcher.find()) {
            System.out.println(" police1: " + policeMatcher.group(policeMatcher.groupCount())); // get last group
        }

        policeMatcher = policeActivePattern.getMatcher(tokens);
        while (policeMatcher.find()) {
            System.out.println(" police2: " + policeMatcher.group(1));
        }

        policeMatcher = policeAllPattern.getMatcher(tokens);
        while (policeMatcher.find()) {
            System.out.println(" police3: " + policeMatcher.group(1));
        }
    }

}
