package com.cse10.gui.task.duplicateDetect;

import com.cse10.duplicateDetector.DuplicateDetectorUIHandler;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-29.
 */
public class DuplicateDetectorTask extends SwingWorker implements Observer {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected boolean done = false;
    protected Thread duplicateDetectorUIHandlerThread;
    protected DuplicateDetectorUIHandler duplicateDetectorUIHandler;

    public DuplicateDetectorTask() {
        this.duplicateDetectorUIHandler = new DuplicateDetectorUIHandler();
        duplicateDetectorUIHandlerThread = new Thread(duplicateDetectorUIHandler);

    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    protected Object doInBackground() {
        if (!done) {
            logger.info("Duplicate Detector -> In Background");
            duplicateDetectorUIHandler.addObserver(this);
            duplicateDetectorUIHandlerThread.start();
            try {
                duplicateDetectorUIHandlerThread.join();
            } catch (InterruptedException e) {

            }
            logger.info("Duplicate Detector ->  Finished Task");
        }

        return null;
    }

    public void stop() {
        duplicateDetectorUIHandlerThread.interrupt();
        logger.info("interrupted by user");
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        logger.info("Duplicate Detector -> Done");
        done = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        int progress = (Integer) arg;
        setProgress(progress);
    }
}
