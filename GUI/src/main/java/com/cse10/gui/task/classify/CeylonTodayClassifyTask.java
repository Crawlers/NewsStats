package com.cse10.gui.task.classify;

import com.cse10.classifier.CeylonTodayClassifierUIHandler;
import java.util.Date;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class CeylonTodayClassifyTask extends ClassifyTask implements Observer{

    public CeylonTodayClassifyTask() {
    }

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

            //start classification process
            classifierUIHandler=new CeylonTodayClassifierUIHandler();
            classifierUIHandler.getClassifierConfigurator().addObserver(this);
            classifierUIHandler.setEndDate(endDate);
            classifierUIHandler.setName("Ceylon Today Classifier Thread");
            classifierUIHandler.run();

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
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_ceylon_today"))
            setProgress(Integer.parseInt(m[1]));
    }
}
