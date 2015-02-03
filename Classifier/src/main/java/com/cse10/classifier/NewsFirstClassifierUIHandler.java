package com.cse10.classifier;

import com.cse10.article.NewsFirstArticle;

/**
 * Created by hp on 2/2/2015.
 */
public class NewsFirstClassifierUIHandler extends ClassifierUIHandler  {

    @Override
    public void run() {
        setName("NewsFirst");
        super.run();
        classifierConfigurator.startClassification(NewsFirstArticle.class,endDate);
    }
}
