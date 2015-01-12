package com.cse10.extractor.stanfordcorenlp;

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.extractor.stanfordcorenlp.detector.*;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by TharinduWijewardane on 2015-01-01.
 */
public class Controller {

    private static StanfordCoreNLP pipeline;

    public static void main(String[] args) {

        List<Article> crimeArticles = loadCrimeArticles();

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);

        for (Article crimeArticle : crimeArticles) {

            System.out.println("---------------------- NEW ARTICLE ----------------------");
            extractEntityGroup((CrimeArticle) crimeArticle);

        }

    }

    private static void extractEntityGroup(CrimeArticle crimeArticle) {

        String text = crimeArticle.getContent();

        text = ContentFilter.manuelFilter(text);

        System.out.println(text);
        System.out.println("---");

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        CrimeEntityGroup crimeEntityGroup = buildCrimeEntityGroup(sentences);

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        Map<Integer, CorefChain> graph =
                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        System.out.println("graph: ");
        System.out.println(graph);


        saveCrimeEntityGroup(crimeEntityGroup);
    }

    private static CrimeEntityGroup buildCrimeEntityGroup(List<CoreMap> sentences) {

        //to manage multiple entities extracted from different sentences
        ArrayList<String> crimeTypes = new ArrayList<String>();
        ArrayList<String> criminals = new ArrayList<String>();
        ArrayList<String> victims = new ArrayList<String>();
        ArrayList<String> locations = new ArrayList<String>();
        ArrayList<String> policeStations = new ArrayList<String>();
        ArrayList<String> courts = new ArrayList<String>();
        ArrayList<String> possessions = new ArrayList<String>();
        ArrayList<String> prisons = new ArrayList<String>();

        for (CoreMap sentence : sentences) {

            if (CrimeSentenceDetector.detectCrimeSentence(sentence)) { //if a crime sentence
                List<CoreLabel> tokens = new ArrayList<CoreLabel>();

                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    // this is the text of the token
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    System.out.println("\nword: " + word);
                    // this is the POS tag of the token
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    System.out.println("pos: " + pos);
                    // this is the NER label of the token
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    System.out.println("ne: " + ne);
                    String lem = token.get(CoreAnnotations.LemmaAnnotation.class);
                    System.out.println("lem: " + lem);

                    tokens.add(token);
                }

                System.out.println(sentence);

                // to filter out empty strings
                checkAndAdd(crimeTypes, CrimeTypeDetector.findCrimeType(tokens));
                checkAndAdd(criminals, CriminalDetector.findCriminal(tokens));
                checkAndAdd(victims, VictimDetector.findVictim(tokens));
                checkAndAdd(locations, LocationDetector.findLocation(tokens));
                checkAndAdd(policeStations, PoliceStationDetector.findPolice(tokens));
                checkAndAdd(courts, CourtDetector.findCourt(tokens));
                checkAndAdd(possessions, PossessionDetector.findPossession(tokens));
                checkAndAdd(prisons, PrisonDetector.findPrison(tokens));

            }


//            // this is the parse tree of the current sentence
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            tree.pennPrint();
//
//            // this is the Stanford dependency graph of the current sentence
//            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//            System.out.println("SemanticGraph: ");
//            dependencies.prettyPrint();
        }

        CrimeEntityGroup crimeEntityGroup = new CrimeEntityGroup();

        // to get most common value (obtained from different sentences)
        crimeEntityGroup.setCrimeType(getMostCommonElement(crimeTypes));
        crimeEntityGroup.setCriminal(getMostCommonElement(criminals));
        crimeEntityGroup.setVictim(getMostCommonElement(victims));
        crimeEntityGroup.setLocation(getMostCommonElement(locations));
        crimeEntityGroup.setPolice(getMostCommonElement(policeStations));
        crimeEntityGroup.setCourt(getMostCommonElement(courts));
        crimeEntityGroup.setPossession(getMostCommonElement(policeStations));
        // prison?

        return crimeEntityGroup;
    }


    private static void saveCrimeEntityGroup(CrimeEntityGroup crimeEntityGroup) {

        DatabaseHandler.insertCrimeEntities(crimeEntityGroup);

    }

    private static List<Article> loadCrimeArticles() {

//        List<Article> crimeArticles = DatabaseHandler.fetchArticles(CrimeArticle.class); //TODO
        List<Article> crimeArticles = DatabaseHandler.fetchArticlesByIdRange(CrimeArticle.class, 151, 160); // for now


        return crimeArticles;
    }

    private static void checkAndAdd(ArrayList<String> list, String keyword) { // for filtering out null and empty values

        if (keyword != null && keyword.length() > 0) {
            list.add(keyword);
        }

    }

    private static String getMostCommonElement(ArrayList<String> list) {

        Collections.sort(list);
        String mostCommon = null;
        String last = null;
        int mostCount = 0;
        int lastCount = 0;
        for (String x : list) {
            System.out.println("in list: " + x);
            if (x.equalsIgnoreCase(last)) {
                lastCount++;
            } else if (lastCount > mostCount) {
                mostCount = lastCount;
                mostCommon = last;
            }
            last = x;
        }
        if (mostCommon == null) {
            mostCommon = last;
        }
        System.out.println("most common: " + mostCommon);
        return mostCommon;

    }
}
