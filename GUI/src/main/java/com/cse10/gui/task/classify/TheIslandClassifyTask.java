package com.cse10.gui.task.classify;


import com.cse10.classifier.TheIslandClassifierUIHandler;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class TheIslandClassifyTask extends ClassifyTask implements Observer {

    public TheIslandClassifyTask() {
    }

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
            Thread.currentThread().setName("The Island Classifier Thread");
            //Initialize progress property.
            setProgress(0);

            //start classification process
            classifierUIHandler=new TheIslandClassifierUIHandler();
            classifierUIHandler.getClassifierConfigurator().addObserver(this);
            classifierUIHandler.setEndDate(endDate);
            classifierUIHandler.setName("The Island Classifier Thread");
            classifierUIHandler.run();
            classifierUIHandler.getClassifierConfigurator().deleteObserver(this);

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
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_the_island"))
            setProgress(Integer.parseInt(m[1]));
    }
}
