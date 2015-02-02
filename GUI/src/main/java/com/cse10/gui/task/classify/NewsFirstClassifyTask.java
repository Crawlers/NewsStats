package com.cse10.gui.task.classify;

import com.cse10.classifier.DailyMirrorClassifierUIHandler;
import com.cse10.classifier.NewsFirstClassifierUIHandler;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class NewsFirstClassifyTask extends ClassifyTask implements Observer{

    public NewsFirstClassifyTask() {
    }

    public NewsFirstClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("NewsFirst Classifer -> In Background");
            Thread.currentThread().setName("NewsFirst Classifier Thread");
            //Initialize progress property.
            setProgress(0);

            //start classification process
            classifierUIHandler = new NewsFirstClassifierUIHandler();
            classifierUIHandler.getClassifierConfigurator().addObserver(this);
            classifierUIHandler.setEndDate(endDate);
            classifierUIHandler.setName("NewsFirst Classifier Thread");
            classifierUIHandler.run();
            classifierUIHandler.getClassifierConfigurator().deleteObserver(this);

            System.out.println("NewsFirst Classifer -> Finished Task");

        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println("NewsFirst Classifer -> Done " +classifierUIHandler.isAlive());
        done = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_news_first"))
            setProgress(Integer.parseInt(m[1]));
    }
}
