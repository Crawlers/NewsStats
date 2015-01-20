package com.cse10.gui.task.crawl;

import javax.swing.*;
import java.util.Date;
import java.util.Observer;

/**
 * Created by TharinduWijewardane on 2015-01-18.
 */
public abstract class CrawlTask extends SwingWorker <Void, Void> implements Observer {

    protected boolean done = false;

    protected Date startDate;
    protected Date endDate;

    public CrawlTask(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
