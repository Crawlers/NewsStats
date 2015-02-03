package com.cse10.gui.task.duplicateDetect;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-29.
 */
public class DuplicateDetectorTask extends SwingWorker implements Observer {

    protected boolean done = false;

    /*
     * Main task. Executed in background thread.
     */
    @Override
    protected Object doInBackground() {
        if (!done) {
            System.out.println("in background");

        }

        return null;
    }

    public void stop() {

    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println("done");
        done = true;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
