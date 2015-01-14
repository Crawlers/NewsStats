package com.cse10.extractor.stanfordcorenlp;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;

import java.util.List;

/**
 * Created by TharinduWijewardane on 2014-12-16.
 */
public class CrimeRegexPatterns {

    //to do Consumer Affairs Authority, unfit for consumption
    //to do core reference
    //to do asylum seeker (15)

    private static String crimeType = "";
    private static String criminal = "";
    private static String victim = "";
    private static String location = "";
    private static String police = "";
    private static String court = "";
    private static String possession = "";
    private static String prison = "";


    public static void printAll(List<CoreLabel> tokens) {

        detectCriminal(tokens);
        printVictim(tokens);
        printSuicide(tokens);
        printLocation(tokens);
        printPolice(tokens);
        printCourt(tokens);
        printPossession(tokens);
        printPrison(tokens);

    }

    protected static TokenSequencePattern criminalPattern = TokenSequencePattern.compile("(/killed|murdered|stabbed|raped/) []{0,4} /by/ []{0,2} ([ner: PERSON]{1,4})");
    protected static TokenSequencePattern criminalActivePattern = TokenSequencePattern.compile("([ner: PERSON]{1,4}) []{0,4} (/killed|murdered|stabbed|raped/)");
    protected static TokenSequencePattern criminalIngPattern = TokenSequencePattern.compile("[{tag:IN}]{1,1} []{0,4} (/killing|murdering|stabbing|raping/)");

    public static void detectCriminal(List<CoreLabel> tokens) {

        if (crimeType.length() > 0 && criminal.length() > 0) {
            return;
        }

        TokenSequenceMatcher criminalMatcher = criminalPattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            crimeType = criminalMatcher.group(1);
            System.out.println(" crime type: " + crimeType);
            criminal = criminalMatcher.group(criminalMatcher.groupCount()); // get last group
            System.out.println(" criminal: " + criminal);
        }

        if (crimeType.length() > 0 && criminal.length() > 0) {
            return;
        }

        criminalMatcher = criminalActivePattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            crimeType = criminalMatcher.group(criminalMatcher.groupCount());
            System.out.println(" crime type: " + crimeType);
            criminal = criminalMatcher.group(1);
            System.out.println(" criminal: " + criminal);
        }

        if (crimeType.length() > 0 && criminal.length() > 0) {
            return;
        }

        criminalMatcher = criminalIngPattern.getMatcher(tokens);
        while (criminalMatcher.find()) {
            System.out.println(" crime type: " + criminalMatcher.group(1));
        }

        if (crimeType.length() > 0 && criminal.length() > 0) {
            return;
        }

    }

    protected static TokenSequencePattern victimPattern = TokenSequencePattern.compile("([{tag:NNS}]{0,3} [{tag:NN}]{0,3} [{tag:NNP}]{0,3}) [{tag:VBD}]{1,2} (/killed|murdered|stabbed|raped/)");

    public static void printVictim(List<CoreLabel> tokens) {

        TokenSequenceMatcher victimMatcher = victimPattern.getMatcher(tokens);
        while (victimMatcher.find()) {
            System.out.println(" crime type: " + victimMatcher.group(victimMatcher.groupCount()));
            System.out.println(" victim: " + victimMatcher.group(1));
        }
    }

    protected static TokenSequencePattern suicidePattern = TokenSequencePattern.compile("([{tag:NNS}]{0,3} [{tag:NN}]{0,3} [{tag:NNP}]{0,3}) /committed/ /suicide/");

    public static void printSuicide(List<CoreLabel> tokens) {

        TokenSequenceMatcher victimMatcher = suicidePattern.getMatcher(tokens);
        while (victimMatcher.find()) {
            System.out.println(" suicide type");
            System.out.println(" victim: " + victimMatcher.group(1));
        }
    }

    protected static TokenSequencePattern caughtPattern = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|remanded|raided/ | /took/ /into/ /custody/ | /taken/ /into/ /custody/ ) []{0,15} [{tag:IN}]{1,2} []{0,3} ([ner: LOCATION])");

    public static void printLocation(List<CoreLabel> tokens) {

        TokenSequenceMatcher caughtMatcher = caughtPattern.getMatcher(tokens);
        while (caughtMatcher.find()) {
            System.out.println(" caught location: " + caughtMatcher.group(caughtMatcher.groupCount())); // get last group
        }

    }

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

    protected static TokenSequencePattern courtPattern = TokenSequencePattern.compile("([{tag:NNP}]{1,3}) /Magistrate?/ []{0,2} /Court/");

    public static void printCourt(List<CoreLabel> tokens) {

        TokenSequenceMatcher courtMatcher = courtPattern.getMatcher(tokens);
        while (courtMatcher.find()) {
            System.out.println(" court: " + courtMatcher.group(1));
        }

    }

    protected static TokenSequencePattern crimePossessionPattern = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|raided/ | /taken/ /into/ /custody/ ) []{0,6} /with/ [{tag:CD}]{0,2} ([{tag:NN}]{0,3} [{tag:NNS}]{0,3})");
    protected static TokenSequencePattern crimePossessionPattern2 = TokenSequencePattern.compile("(/arrested|apprehended|nabbed|seized|raided/ | /taken/ /into/ /custody/ ) []{0,6} /for|in/ /possession/ /of/ [{tag:CD}]{0,2} ([{tag:NN}]{0,3} [{tag:NNS}]{0,3})");
    protected static TokenSequencePattern crimePossessionPattern3 = TokenSequencePattern.compile("[{tag:CD}]{0,2} []{0,4} ([{tag:JJ}]{0,2} [{tag:NNS}]{1,3}) []{0,4} /seized|captured/ []{0,2} /in|from/ []{0,3} /possession/");

    public static void printPossession(List<CoreLabel> tokens) {

        TokenSequenceMatcher possessionMatcher = crimePossessionPattern.getMatcher(tokens);
        while (possessionMatcher.find()) {
            System.out.println(" possession: " + possessionMatcher.group(possessionMatcher.groupCount())); // get last group
        }

        possessionMatcher = crimePossessionPattern2.getMatcher(tokens);
        while (possessionMatcher.find()) {
            System.out.println(" possession: " + possessionMatcher.group(possessionMatcher.groupCount())); // get last group
        }

        possessionMatcher = crimePossessionPattern3.getMatcher(tokens);
        while (possessionMatcher.find()) {
            System.out.println(" possession: " + possessionMatcher.group(possessionMatcher.groupCount())); // get last group
        }

    }

    protected static TokenSequencePattern prisonPattern = TokenSequencePattern.compile("/escaped/ /from/ ([ner: LOCATION]) /prison/");

    public static void printPrison(List<CoreLabel> tokens) {

        TokenSequenceMatcher prisonMatcher = prisonPattern.getMatcher(tokens);
        while (prisonMatcher.find()) {
            System.out.println(" prison: " + prisonMatcher.group(prisonMatcher.groupCount())); // get last group
        }
    }

}
