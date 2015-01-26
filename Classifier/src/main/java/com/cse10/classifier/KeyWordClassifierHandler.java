package com.cse10.classifier;

import com.cse10.gate.DocumentKeyWordFinder;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;

/**
 * remove obvious non-crime articles
 * this is working with raw article data
 * Created by chamath on 12/21/2014.
 */
public class KeyWordClassifierHandler extends ClassifierHandler{

    private DocumentKeyWordFinder documentKeyWordFinder;
    private NGramTokenizer tokenizer;
    private StanfordCoreNLPLemmatizer lemmatizer;

    public KeyWordClassifierHandler() {
        this.documentKeyWordFinder = new DocumentKeyWordFinder();
        this.tokenizer = new NGramTokenizer();
        this.lemmatizer = new StanfordCoreNLPLemmatizer();
    }

    /**
     * configure classifier
     * @param minTokenSize
     * @param maxTokenSize
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
     */
    public void crossValidateClassifier(Instances trainingData){
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
                System.out.println("crime correct");
                crimeCorrectCount++;
            }else if(label.equals("crime") && !exist){
                System.out.println("crime incorrect");
                crimeIncorrectCount++;
            }else if(label.equals("other") && !exist){
                System.out.println("non crime correct");
                otherCorrectCount++;
            }else{
                System.out.println("non crime incorrect");
                otherIncorrectCount++;
            }
        }

        System.out.println("Crime Accuracy= "+ ((crimeCorrectCount/(crimeCorrectCount+crimeIncorrectCount))*100)+"%");
        System.out.println("Other Accuracy= " + ((otherCorrectCount / (otherCorrectCount + otherIncorrectCount)) * 100) + "%");
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
        //System.out.println("WORDS OF CONTENT ===" + words);
        exist = documentKeyWordFinder.isKeyWordExist(words);
        if(exist)
            return 0.0;
        else
            return 1.0;
    }
}
