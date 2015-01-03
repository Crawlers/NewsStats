package com.cse10.extractor.stanfordcorenlp;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by TharinduWijewardane on 2014-12-10.
 */
public class StanfordCoreNlpMy {

    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

//        // read some text in the text variable
//        String text0 = "This is a test statement. Siripala was killed in Buthpitiya"; // Add your text here!
//
//        String text00 = "Two people have been killed by Nimal. Four suspects who were taken into custody by Gampaha Colomb police with 96 counterfeit currency notes worth Rs. 408,000 in Mihinthale were remanded by the Anuradhapura Magistrate’s Court, today. The suspects were ordered to be remanded till 11 January after officials of the Thirappane Police station produced them before the Chief Magistrate. Around 78 fake notes of Rs. 5000 and 18 fake notes of Rs.1000 were seized from the suspect’s possession. Police sources said a fake gem which was in one of a suspect’s possession was recovered as well. The remanded suspects will be produced in Court again on 11 January. (Ceylon Today Online)";

        String text = "";
        try {
            text = FileUtils.readFileToString(new File("E:\\PROJECT-workspace\\Entity-extraction\\data\\temp\\21.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        text = text.replaceAll(" Rs. ", " Rs ");

        System.out.println(text);
        System.out.println("---");

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<CoreMap> crimeSentences = new ArrayList<CoreMap>();

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
                CrimeRegexPatterns.printAll(tokens);

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

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        Map<Integer, CorefChain> graph =
                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        System.out.println("graph: ");
        System.out.println(graph);

    }


}
