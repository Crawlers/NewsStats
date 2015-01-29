package com.cse10.gui.task.classify;

import com.cse10.classifier.DailyMirrorClassifierUIHandler;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class DailyMirrorClassifyTask extends ClassifyTask implements Observer {

    public DailyMirrorClassifyTask() {
    }

    public DailyMirrorClassifyTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("DailyMirror Classifer -> In Background");
            Thread.currentThread().setName("Daily Mirror Classifier Thread");
            //Initialize progress property.
            setProgress(0);

            //start classification process
            classifierUIHandler = new DailyMirrorClassifierUIHandler();
            classifierUIHandler.getClassifierConfigurator().addObserver(this);
            classifierUIHandler.setEndDate(endDate);
            classifierUIHandler.setName("Daily Mirror Classifier Thread");
            classifierUIHandler.run();

            System.out.println("DailyMirror Classifer -> Finished Task");

        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println("DailyMirror Classifer -> Done " +classifierUIHandler.isAlive());
        done = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        String[] m = message.split(" ");
        if (m[0].equals("article_daily_mirror"))
            setProgress(Integer.parseInt(m[1]));
    }
}
