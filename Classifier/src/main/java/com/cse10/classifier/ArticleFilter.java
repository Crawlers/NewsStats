package com.cse10.classifier;

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
public class ArticleFilter {

    protected StringToWordVector filter;
    protected SnowballStemmer stemmer;
    protected  StanfordCoreNLPLemmatizer stanfordCoreNLPLemmatizer;

    public ArticleFilter(){
        filter=new StringToWordVector();
        stemmer = new SnowballStemmer();
        stemmer.setStemmer("english");
       // stanfordCoreNLPLemmatizer = new StanfordCoreNLPLemmatizer();
    }

    public void configure(int minNGramSize,int maxNGramSize,boolean useStemmer){
        //set tokenizer - we can specify n-grams for classification
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(minNGramSize);
        tokenizer.setNGramMaxSize(maxNGramSize);
        tokenizer.setDelimiters("\\W");
        //set stemmer or lemmatizer
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

    }

    public void setInputFormat(Instances instances){
        try {
            filter.setInputFormat(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform articles to feature vectors and save in a file
     * @param instances
     *
     */
    public void saveTransformedArticlesToFile(Instances instances)  {


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