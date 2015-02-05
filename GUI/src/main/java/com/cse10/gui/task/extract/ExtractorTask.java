package com.cse10.gui.task.extract;

import com.cse10.extractor.gate.EntityExtractorTask;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-29.
 */
public class ExtractorTask extends SwingWorker implements Observer {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected boolean done = false;
    private Thread extractorThread;

    /*
     * Main task. Executed in background thread.
     */
    @Override
    protected Object doInBackground() {
        if (!done) {
            logger.info("in background");

            EntityExtractorTask entityExtractorTask = new EntityExtractorTask();
            entityExtractorTask.getEntityExtrator().addObserver(this);
            extractorThread = new Thread(entityExtractorTask);
            extractorThread.start();

            try {
                extractorThread.join();
                entityExtractorTask.getEntityExtrator().deleteObserver(this);
            } catch (InterruptedException e) {
                logger.info("Error: ", e);
            }
        }

        return null;
    }

    public void stopExtract() {
        extractorThread.interrupt();
        logger.info("interrupted by user");
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        logger.info("done");
        done = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        int progress = ((Integer) arg).intValue();
        setProgress(progress);
    }
}
