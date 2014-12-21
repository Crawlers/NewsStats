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
public class KeyWordClassifierHandler {

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
        GenericDataHandler genericDataHandler=new GenericDataHandler();
        Instances instances=genericDataHandler.loadTrainingData();
        String words;
        boolean exist=false;
        int crimeCorrectCount=0;
        int crimeIncorrectCount=0;
        int otherCorrectCount=0;
        int otherIncorrectCount=0;
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
                crimeCorrectCount++;
            }else if(label.equals("crime") && !exist){
                crimeIncorrectCount++;
            }else if(label.equals("other") && !exist){
                otherCorrectCount++;
            }else{
                otherIncorrectCount++;
            }
        }

        System.out.println("Crime Accuracy= "+ (crimeCorrectCount/(crimeCorrectCount+crimeIncorrectCount))+"%");
        System.out.println("Other Accuracy= "+ (otherCorrectCount/(otherCorrectCount+otherIncorrectCount))+"%");
    }

    /**
     * instance is raw article data
     * @param instance
     * @return
     */
    public double classifyInstance(Instance instance){
        String content = instance.stringValue(0);
        documentKeyWordFinder.isKeyWordExist(content);
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


}
