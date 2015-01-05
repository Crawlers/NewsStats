package com.cse10.extractor.stanfordcorenlp.detector;

import com.cse10.extractor.ExtractorConstants;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2015-01-05.
 */
public class CrimeTypeDetector {

    private static TokenSequencePattern criminalPattern = TokenSequencePattern.compile("(/killed|murdered|stabbed|raped/) []{0,4} /by/ []{0,2} ([ner: PERSON]{1,4})");
    private static TokenSequencePattern criminalActivePattern = TokenSequencePattern.compile("([ner: PERSON]{1,4}) []{0,4} (/killed|murdered|stabbed|raped/)");
    private static TokenSequencePattern criminalIngPattern = TokenSequencePattern.compile("[{tag:IN}]{1,1} []{0,4} (/killing|murdering|stabbing|raping/)");
    private static TokenSequencePattern victimPattern = TokenSequencePattern.compile("([{tag:NNS}]{0,3} [{tag:NN}]{0,3} [{tag:NNP}]{0,3}) [{tag:VBD}]{1,2} (/killed|murdered|stabbed|raped/)");
    protected static TokenSequencePattern suicidePattern = TokenSequencePattern.compile("([{tag:NNS}]{0,3} [{tag:NN}]{0,3} [{tag:NNP}]{0,3}) /committed/ /suicide/");

    public static String findCrimeType(List<CoreLabel> tokens) {

        String crimeType = "";

        TokenSequenceMatcher criminalMatcher = criminalPattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            crimeType = criminalMatcher.group(1);
            System.out.println(" crime type: " + crimeType);
        }

        if (crimeType.length() > 0) {
            return crimeType;
        }

        criminalMatcher = criminalActivePattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            crimeType = criminalMatcher.group(criminalMatcher.groupCount());
            System.out.println(" crime type: " + crimeType);
        }

        if (crimeType.length() > 0) {
            return crimeType;
        }

        criminalMatcher = criminalIngPattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            crimeType = criminalMatcher.group(1);
            System.out.println(" crime type: " + crimeType);
        }

        if (crimeType.length() > 0) {
            return crimeType;
        }

        TokenSequenceMatcher victimMatcher = victimPattern.getMatcher(tokens);
        while (victimMatcher.find()) {
            crimeType = victimMatcher.group(victimMatcher.groupCount());
            System.out.println(" crime type: " + crimeType);
        }

        TokenSequenceMatcher suicideVictimMatcher = suicidePattern.getMatcher(tokens);
        while (suicideVictimMatcher.find()) {
            crimeType = ExtractorConstants.CRIME_TYPE_SUICIDE;
            System.out.println(" suicide type");
        }

        return crimeType;

    }

}
