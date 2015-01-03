package com.cse10.extractor.stanfordcorenlp;

import edu.stanford.nlp.util.CoreMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TharinduWijewardane on 2014-12-16.
 */
public class CrimeSentenceDetector {

    private static String killedSentenceRegex = " kill | killed | killing | dead | murder | murdered | murdering | behead | beheaded | beheading | execute | executed | executing | execution | hanged | suicide | commit suicide | committed suicide | death at the hands of ";
    private static String rapedSentenceRegex = " rape | raped | raping ";
    private static String caughtSentenceRegex = " arrest | arrested | apprehend | apprehended | nab | nabbed | seize | seized | remand | remanded | raid | raided | take into custody | took into custody | taken into custody ";
    private static String courtSentenceRegex = " Magistrate’s Court ";
    private static String prisonSentenceRegex = " prison ";

    private static String sentenceRegex = killedSentenceRegex + "|" + rapedSentenceRegex + "|" + caughtSentenceRegex + "|" + courtSentenceRegex + "|" + prisonSentenceRegex;
    private static Pattern crimeSentencePattern = Pattern.compile(sentenceRegex);

    public static boolean detectCrimeSentence(CoreMap sentence) {

        Matcher crimeSentenceMatcher = crimeSentencePattern.matcher(sentence.toString());
        if (crimeSentenceMatcher.find()) { //if a crime sentence
            return true;
        }

        return false;
    }

}
