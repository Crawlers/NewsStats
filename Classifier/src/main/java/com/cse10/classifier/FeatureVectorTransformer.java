package com.cse10.classifier;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * used to transfer document features into vectors
 * Created by Chamath on 12/16/2014.
 */

public class FeatureVectorTransformer {

    protected StringToWordVector filter;
    private Logger log;

    public FeatureVectorTransformer() {
        filter = new StringToWordVector();
        log = Logger.getLogger(this.getClass());
    }

    /**
     * configure filter
     *
     * @param minNGramSize
     * @param maxNGramSize
     * @param useStemmer
     */
    public void configure(int minNGramSize, int maxNGramSize, boolean useStemmer) {

        log.info("\n Feature Vector Transformer -> Configuration Started");
        //set tokenizer - we can specify n-grams for classification
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(minNGramSize);
        tokenizer.setNGramMaxSize(maxNGramSize);
        tokenizer.setDelimiters("\\W");
        //set stemmer or lemmatizer
        if (useStemmer) {
            SnowballStemmer stemmer = new SnowballStemmer();
            stemmer.setStemmer("english");
            filter.setStemmer(stemmer);
        } else {
            StanfordCoreNLPLemmatizer lemmatizer;
            lemmatizer = new StanfordCoreNLPLemmatizer();
            filter.setStemmer(lemmatizer);
        }
        //create new filter for vector transformation
        filter.setLowerCaseTokens(true);
        filter.setOutputWordCounts(true);
        filter.setTFTransform(true);
        filter.setIDFTransform(true);
        filter.setStopwords(new File("Classifier\\src\\main\\resources\\StopWordsR4.txt"));
        filter.setTokenizer(tokenizer);
        log.info("\n Feature Vector Transformer -> Configuration Completed");

    }

    /**
     * set input format of the filter
     *
     * @param instances
     */
    public void setInputFormat(Instances instances) {
        try {
            filter.setInputFormat(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform articles to feature vectors and save in a file
     *
     * @param instances
     * @param fileName
     */
    public Instances getTransformedArticles(Instances instances, String fileName) {

        log.info("\n Feature Vector Transformer -> Start Getting Transformed Articles");
        Instances dataFiltered;
        // apply the StringToWordVector filter
        try {
            dataFiltered = weka.filters.Filter.useFilter(instances, filter);
        } catch (Exception e) {
            dataFiltered = instances;
            e.printStackTrace();
        }

        //save to file
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataFiltered);
        try {
            String path = "Classifier\\src\\main\\resources\\arffData\\".concat(fileName);
            saver.setFile(new File(path));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("\n Feature Vector Transformer -> Finish Getting Transformed Articles");
        return dataFiltered;

    }


    /**
     * @param instances
     * @return
     */
    public Instances getTransformedArticles(Instances instances) {
        log.info("\n Feature Vector Transformer -> Start Getting Transformed Articles");
        Instances dataFiltered;
        try {
            dataFiltered = weka.filters.Filter.useFilter(instances, filter);
        } catch (Exception e) {
            dataFiltered = instances;
            e.printStackTrace();
        }
        log.info("\n Feature Vector Transformer -> Finish Getting Transformed Articles");
        return dataFiltered;
    }


    //for functional testing
    public StringToWordVector getFilter() {
        return filter;
    }
}
