package com.cse10.classifier;

import com.cse10.article.TheIslandArticle;

/**
 * UI handler for the Island classifier
 * Created by Chamath on 1/27/2015.
 */
public class TheIslandClassifierUIHandler extends ClassifierUIHandler {

    @Override
    public void run() {
        setName("TheIsland");
        super.run();
        classifierConfigurator.startClassification(TheIslandArticle.class, endDate);
    }
}
