package com.cse10.gui.task.classify;

import com.cse10.classifier.ClassifierUIHandler;

import javax.swing.*;
import java.util.Date;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-19.
 */
public abstract class ClassifyTask extends SwingWorker<Void, Void> implements Observer {

    protected boolean done = false;
    protected ClassifierUIHandler classifierUIHandler;
    protected Date startDate;
    protected Date endDate;

    public ClassifyTask() {
    }

    public ClassifyTask(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println(getPaperName() + " -> In Background");
            Thread.currentThread().setName(getPaperName() + " Classifier Thread");
            //Initialize progress property.
            setProgress(0);

            //start classification process
            classifierUIHandler = getClassifierUIHandler();
            classifierUIHandler.getClassifierConfigurator().addObserver(this);
            classifierUIHandler.setEndDate(endDate);
            classifierUIHandler.setName(getPaperName() + " Classifier Thread");
            classifierUIHandler.run();
            classifierUIHandler.getClassifierConfigurator().deleteObserver(this);

            System.out.println(getPaperName() + " Classifer -> Finished Task");

        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println(getPaperName() + " Classifer -> Done");
        done = true;
    }

    protected abstract ClassifierUIHandler getClassifierUIHandler(); //tobe implemented in paper specific subclasses

    protected abstract String getPaperName(); //tobe implemented in paper specific subclasses

    //to stop classification process
    public void stopClassification() {
        classifierUIHandler.getClassifierConfigurator().stopClassification();
        classifierUIHandler.interrupt();
    }

}
