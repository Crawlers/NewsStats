package com.cse10.gui.task.classify;

import com.cse10.article.CeylonTodayArticle;
import com.cse10.classifier.ClassifierUIHandler;
import java.util.Date;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class CeylonTodayClassifyTask extends ClassifyTask implements Observer{

    public CeylonTodayClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("Ceylon Today Classifer -> In Background");
            Thread.currentThread().setName("Ceylon Today Classifier Thread");
            //Initialize progress property.
            setProgress(0);

            //classification process
            ClassifierUIHandler classifierUIHandler=ClassifierUIHandler.getInstance();
            classifierUIHandler.addObserver(this);
            classifierUIHandler.buildClassifier();
            classifierUIHandler.classifyNewsArticles(CeylonTodayArticle.class);

            System.out.println("Ceylon Today Classifer -> Finished Task");
        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println("Ceylon Today Classifer -> Done");
        done = true;
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        int value=(Integer)arg;
        setProgress(value);
    }
}
