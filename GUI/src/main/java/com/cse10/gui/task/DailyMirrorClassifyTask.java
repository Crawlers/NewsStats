package com.cse10.gui.task;

import javax.swing.*;
import java.util.Random;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class DailyMirrorClassifyTask extends SwingWorker<Void, Void> {
    /*
     * Main task. Executed in background thread.
     */
    boolean done = false;

    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("in background");
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
        }
        return null;
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
