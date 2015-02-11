package com.cse10.classifier;

import com.cse10.article.CeylonTodayArticle;


/**
 * UI handler class for CeylonToday
 * Created by Chamath on 1/27/2015.
 */
public class CeylonTodayClassifierUIHandler extends ClassifierUIHandler {

    @Override
    public void run() {
        setName("CeylonToday");
        super.run();
        classifierConfigurator.startClassification(CeylonTodayArticle.class, endDate);
    }
}
