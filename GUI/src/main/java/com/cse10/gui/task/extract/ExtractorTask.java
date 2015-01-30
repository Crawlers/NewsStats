package com.cse10.gui.task.extract;

import com.cse10.extractor.gate.EntityExtractorTask;

import javax.swing.*;

/**
 * Created by TharinduWijewardane on 2015-01-29.
 */
public class ExtractorTask extends SwingWorker {

    protected boolean done = false;
    private Thread extractorThread;

    /*
     * Main task. Executed in background thread.
     */
    @Override
    protected Object doInBackground() {
        if (!done) {
            System.out.println("in background");
            EntityExtractorTask entityExtractorThread = new EntityExtractorTask();
            extractorThread = new Thread(entityExtractorThread);
            extractorThread.start();
        }

        return null;
    }

    public void stopExtract() {
        extractorThread.interrupt();
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        System.out.println("done");
        done = true;
    }
}
