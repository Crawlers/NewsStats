package com.cse10.classifier;

import com.cse10.article.DailyMirrorArticle;

/**
 * Created by chamath on 1/27/2015.
 */
public class DailyMirrorClassifierUIHandler extends ClassifierUIHandler{

    @Override
    public void run() {
        setName("DailyMirror");
        super.run();
        classifierConfigurator.startClassification(DailyMirrorArticle.class);
    }
}
