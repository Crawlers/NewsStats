package com.cse10.classifier;

import com.cse10.gate.DocumentKeyWordFinder;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * remove obvious non-crime articles
 * this is working with raw article data
 * Created by Chamath on 12/21/2014.
 */
public class KeyWordClassifierHandler extends ClassifierHandler{

    private DocumentKeyWordFinder documentKeyWordFinder;
    private NGramTokenizer tokenizer;
    private StanfordCoreNLPLemmatizer lemmatizer;
    private Logger log;

    public KeyWordClassifierHandler() {
        this.documentKeyWordFinder = new DocumentKeyWordFinder();
        this.tokenizer = new NGramTokenizer();
        this.lemmatizer = new StanfordCoreNLPLemmatizer();
        log = Logger.getLogger(this.getClass());
    }

    /**
     * configure classifier
     * @param minTokenSize minimum token size
     * @param maxTokenSize maximum token size
     * @param delimiter
     */
    public void configure(int minTokenSize,int maxTokenSize, String delimiter ){
        tokenizer.setNGramMinSize(minTokenSize);
        tokenizer.setNGramMaxSize(maxTokenSize);
        tokenizer.setDelimiters(delimiter);
    }

    /**
     * cross validate the model, training data should be raw data
     * @param trainingData
     * @return
     */
    public List<Double> crossValidateClassifier(Instances trainingData){
        List<Double> accuracyValues=new ArrayList<>();
        String words;
        boolean exist=false;
        double crimeCorrectCount=0;
        double crimeIncorrectCount=0;
        double otherCorrectCount=0;
        double otherIncorrectCount=0;
        for(int i=0;i<trainingData.numInstances();i++) {
            words = "";
            String content = trainingData.instance(i).stringValue(0);
            String label = trainingData.instance(i).stringValue(1);
            tokenizer.tokenize(content);
            while (tokenizer.hasMoreElements()) {
                String element = (String) tokenizer.nextElement();
                words = words.concat(lemmatizer.stem(element));
                words = words.concat(" ");
            }

            exist = documentKeyWordFinder.isKeyWordExist(words);
            if(label.equals("crime") && exist){
                log.info("crime correct");
                crimeCorrectCount++;
            }else if(label.equals("crime") && !exist){
                log.info("crime incorrect");
                crimeIncorrectCount++;
            }else if(label.equals("other") && !exist){
                log.info("non crime correct");
                otherCorrectCount++;
            }else{
                log.info("non crime incorrect");
                otherIncorrectCount++;
            }
        }
        accuracyValues.add((crimeCorrectCount/(crimeCorrectCount+crimeIncorrectCount)) * 100);
        accuracyValues.add((otherCorrectCount / (otherCorrectCount + otherIncorrectCount)) * 100);
        log.info("Crime Accuracy= " + ((crimeCorrectCount / (crimeCorrectCount + crimeIncorrectCount)) * 100) + "%");
        log.info("Other Accuracy= " + ((otherCorrectCount / (otherCorrectCount + otherIncorrectCount)) * 100) + "%");
        return accuracyValues;
    }

    /**
     * instance is raw article data
     * @param testInstance
     * @return
     */
    public double classifyInstance(Instance testInstance){
        String content = testInstance.stringValue(0);

        tokenizer.tokenize(content);
        String words="";
        Boolean exist=false;
        while (tokenizer.hasMoreElements()) {
            String element = (String) tokenizer.nextElement();
            words = words.concat(lemmatizer.stem(element));
            words = words.concat(" ");
        }
        exist = documentKeyWordFinder.isKeyWordExist(words);
        if(exist)
            return 0.0;
        else
            return 1.0;
    }

    //for functional testing
    public NGramTokenizer getTokenizer(){
        return tokenizer;
    }
}
