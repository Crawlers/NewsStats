package com.cse10.gui.task.classify;

import com.cse10.article.TheIslandArticle;
import com.cse10.classifier.ClassifierUIHandler;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class TheIslandClassifyTask extends ClassifyTask implements Observer {

    public TheIslandClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("The Island Classifer -> In Background");

            //Initialize progress property.
            setProgress(0);
            ClassifierUIHandler classifierUIHandler=ClassifierUIHandler.getInstance();
            classifierUIHandler.addObserver(this);
            classifierUIHandler.buildClassifier();
            classifierUIHandler.classifyNewsArticles(TheIslandArticle.class);
            System.out.println("The Island Classifer -> In Background");
        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println("The Island Classifer -> Done");
        done = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        int value=(Integer)arg;
        setProgress(value);
    }
}
