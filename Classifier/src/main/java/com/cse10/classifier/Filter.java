package com.cse10.classifier;

import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.io.IOException;

/**
 * Created by chamath on 12/16/2014.
 */
public class Filter {
    protected StringToWordVector filter;
    protected SnowballStemmer stemmer;
    protected  StanfordCoreNLPLemmatizer stanfordCoreNLPLemmatizer;

    public Filter(){
        filter=new StringToWordVector();
        stemmer = new SnowballStemmer();
        stemmer.setStemmer("english");
        stanfordCoreNLPLemmatizer = new StanfordCoreNLPLemmatizer();
    }

    /**
     * Transform article set to feature vectors
     * @param minNGramSize
     * @param maxNGramSize
     * @param useStemmer
     * @param instances
     * @return
     *
     */
    public Instances getTransformedArticles(int minNGramSize,int maxNGramSize,boolean useStemmer,Instances instances) {

        //set tokenizer - we can specify n-grams for classification
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(minNGramSize);
        tokenizer.setNGramMaxSize(maxNGramSize);
        tokenizer.setDelimiters("\\W");
        if (useStemmer) {
            filter.setStemmer(stemmer);
        } else {
            filter.setStemmer(stanfordCoreNLPLemmatizer);
        }
        //create new filter for vector transformation
        filter.setLowerCaseTokens(true);
        filter.setOutputWordCounts(true);
        filter.setTFTransform(true);
        filter.setIDFTransform(true);
        filter.setStopwords(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\StopWordsR4.txt"));
        filter.setTokenizer(tokenizer);
        Instances dataFiltered;
        // apply the StringToWordVector filter
        try {
            filter.setInputFormat(instances);
            dataFiltered = weka.filters.Filter.useFilter(instances, filter);
        } catch (Exception e) {
            dataFiltered=instances;
            e.printStackTrace();
        }
        return dataFiltered;
    }

    /**
     * Transform articles to feature vectors and save in a file
     * @param minNGramSize
     * @param maxNGramSize
     * @param useStemmer
     * @param instances
     *
     */
    public void saveTransformedArticlesToFile(int minNGramSize,int maxNGramSize,boolean useStemmer,Instances instances)  {

        //set tokenizer - we can specify n-grams for classification
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(minNGramSize);
        tokenizer.setNGramMaxSize(maxNGramSize);
        tokenizer.setDelimiters("\\W");

        if (useStemmer) {
            filter.setStemmer(stemmer);
        } else {
            filter.setStemmer(stanfordCoreNLPLemmatizer);
        }


        //create new filter for vector transformation

        filter.setLowerCaseTokens(true);
        filter.setOutputWordCounts(true);
        filter.setTFTransform(true);
        filter.setIDFTransform(true);
        filter.setStopwords(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\StopWordsR4.txt"));
        filter.setTokenizer(tokenizer);

        Instances dataFiltered = null;
        // apply the StringToWordVector filter
        try {
            filter.setInputFormat(instances);
            dataFiltered = weka.filters.Filter.useFilter(instances, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //save to file
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataFiltered);
        try {
            saver.setFile(new File("C:\\Users\\hp\\Desktop\\SVM implementation\\arffData\\trainingDataBiWords.arff"));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Instances getTransformedArticles(Instances instances){
        Instances dataFiltered;
        try {
            dataFiltered = weka.filters.Filter.useFilter(instances, filter);
        } catch (Exception e) {
            dataFiltered=instances;
            e.printStackTrace();
        }
        return dataFiltered;
    }
}
