package com.cse10.duplicateDetector;

import weka.core.tokenizers.NGramTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chamath on 1/2/2015.
 */
public class FullWordSegmenter  extends WordSegmenter{

    private NGramTokenizer nGramTokenizer;

    public FullWordSegmenter() {
        nGramTokenizer=new NGramTokenizer();
        nGramTokenizer.setNGramMaxSize(1);
        nGramTokenizer.setNGramMinSize(1);
    }

    /**
     * tokenize the given string into words
     * @param document
     * @return
     */
    @Override
    protected List<String> getWords(String document) {

        ArrayList<String> words = new ArrayList();
        nGramTokenizer.tokenize(document);
        String token="";
        while(nGramTokenizer.hasMoreElements()){
            token= (String)nGramTokenizer.nextElement();
            token=token.toLowerCase();
            words.add(token);
        }
        return words;
    }
}
