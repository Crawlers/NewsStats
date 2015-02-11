package com.cse10.classifier;

import com.cse10.article.DailyMirrorArticle;

/**
 * UI handler class for DailyMirror
 * Created by Chamath on 1/29/2015.
 */
public class DailyMirrorClassifierUIHandler extends ClassifierUIHandler {

    @Override
    public void run() {
        setName("DailyMirror");
        super.run();
        classifierConfigurator.startClassification(DailyMirrorArticle.class, endDate);
    }

}
