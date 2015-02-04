package com.cse10.gui.task.analyze;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-02-04.
 */
public class AnalyzeTask extends SwingWorker<Void, Void> implements Observer {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected boolean done = false;

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            logger.info("In Background");

            //todo

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
        //todo. updates for progressbar. send an int value (between 1 and 100) via arg. 100 means completion
    }
}
