package com.cse10.duplicateDetector;

import java.util.List;

/**
 * Created by Chamath on 1/2/2015.
 */
public abstract class WordSegmenter {

    protected abstract List<String> getWords(String document);
}
