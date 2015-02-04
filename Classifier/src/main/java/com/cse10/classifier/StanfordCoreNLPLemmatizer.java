package com.cse10.classifier;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import weka.core.OptionHandler;
import weka.core.stemmers.Stemmer;

import java.io.Serializable;
import java.util.*;

/**
 * Wrapper class for StanfordCoreNLP
 * Created by Chamath on 12/20/2014
 */
public class StanfordCoreNLPLemmatizer implements Stemmer, OptionHandler, Serializable {

    protected StanfordCoreNLP pipeline;
    protected String currentVersion;
    protected String[] options;

    public StanfordCoreNLPLemmatizer() {

        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);

        currentVersion="1.0";

        options=new String[2];
        options[0]="-S";
        options[1]="Stanford Core NLP";
    }

    /**
     * Convert given word into its base form
     * @param word
     * @return
     */
    @Override
    public String stem(String word) {
        List<String> lemmas = new LinkedList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(word);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }
        return lemmas.get(0);

    }

    @Override
    public String getRevision() {
        return currentVersion;
    }

    @Override
    public Enumeration listOptions() {
        Vector v=new Vector();
        for(String s:options){
            v.add(s);
        }
        return v.elements();
    }

    @Override
    public void setOptions(String[] options) throws Exception {
            this.options=options;
    }

    @Override
    public String[] getOptions() {
        /*ArrayList result;
        result = new ArrayList();
        result.add("-S");
        result.add("Stanford Core NLP");
        return (String[]) result.toArray(new String[result.size()]);*/
        return options;
    }

}
