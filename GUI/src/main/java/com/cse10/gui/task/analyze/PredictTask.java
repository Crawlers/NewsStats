package com.cse10.gui.task.analyze;

import com.cse10.analyzer.AnalyzerController;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-02-04.
 */
public class PredictTask extends SwingWorker<Void, Void> implements Observer {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected boolean done = false;

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            logger.info("In Background");

            AnalyzerController analyzerController = AnalyzerController.getInstance();
            analyzerController.addObserver(this);
            analyzerController.predict();
            analyzerController.deleteObserver(this);

        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        done = true;
    }

    public void stop() {
        //todo. code for user to interrupt the operation (if necessary)
    }

    @Override
    public void update(Observable o, Object arg) {
        int progress = ((Integer) arg).intValue();
        setProgress(progress);
    }
}
