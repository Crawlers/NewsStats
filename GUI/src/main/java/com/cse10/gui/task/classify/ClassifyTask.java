package com.cse10.gui.task.classify;

import com.cse10.classifier.ClassifierUIHandler;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Date;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-19.
 */
public abstract class ClassifyTask extends SwingWorker<Void, Void> implements Observer {

    protected Logger logger = Logger.getLogger(this.getClass());

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
            logger.info(getPaperName() + " -> In Background");
            Thread.currentThread().setName(getPaperName() + " Classifier Thread");
            //Initialize progress property.
            setProgress(0);

            //start classification process
            classifierUIHandler = getClassifierUIHandler();
            classifierUIHandler.getClassifierConfigurator().addObserver(this);
            classifierUIHandler.setEndDate(endDate);
            classifierUIHandler.setName(getPaperName() + " Classifier Thread");
            classifierUIHandler.run();
            try {
                classifierUIHandler.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            classifierUIHandler.getClassifierConfigurator().deleteObserver(this);

            logger.info(getPaperName() + " Classifer -> Finished Task");

        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        logger.info(getPaperName() + " Classifer -> Done");
        done = true;
    }

    protected abstract ClassifierUIHandler getClassifierUIHandler(); //tobe implemented in paper specific subclasses

    protected abstract String getPaperName(); //tobe implemented in paper specific subclasses

    //to stop classification process
    public void stopClassification() {
        classifierUIHandler.interrupt();
    }

}
