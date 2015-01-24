package com.cse10.gui.task.classify;

import javax.swing.*;
import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-19.
 */
public abstract class ClassifyTask extends SwingWorker<Void, Void> {

    protected boolean done = false;

    protected Date startDate;
    protected Date endDate;

    public ClassifyTask(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
