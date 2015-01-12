package com.cse10.extractor.stanfordcorenlp.detector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class PoliceStationDetector {

    private static TokenSequencePattern policePattern = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|remanded|raided/ | /taken/ /into/ /custody/ ) []{0,8} /by/ [/the/]{0,1} ([{tag:NNP}]{1,3}) /police|Police/");
    private static TokenSequencePattern policeActivePattern = TokenSequencePattern.compile("([{tag:NNP}]{1,3}) /police|Police/ []{0,4} (/arrested|apprehended|nabbed|seized|remanded|raided/ | /took/ /into/ /custody/ )");
    private static TokenSequencePattern policeAllPattern = TokenSequencePattern.compile("([{tag:NNP}]{1,3}) /police|Police/");

    public static String findPolice(List<CoreLabel> tokens) {

        String policeStation = "";

        TokenSequenceMatcher policeMatcher = policePattern.getMatcher(tokens);
        while (policeMatcher.find()) {
            policeStation = policeMatcher.group(policeMatcher.groupCount()); // get last group
            System.out.println(" police1: " + policeStation);
        }

        if (policeStation.length() > 0) {
            return policeStation;
        }

        policeMatcher = policeActivePattern.getMatcher(tokens);
        while (policeMatcher.find()) {
            policeStation = policeMatcher.group(1);
            System.out.println(" police2: " + policeStation);
        }

        if (policeStation.length() > 0) {
            return policeStation;
        }

        policeMatcher = policeAllPattern.getMatcher(tokens);
        while (policeMatcher.find()) {
            policeStation = policeMatcher.group(1);
            System.out.println(" police3: " + policeStation);
        }

        return policeStation;
    }

}
