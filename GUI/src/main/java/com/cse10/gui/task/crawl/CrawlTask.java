package com.cse10.gui.task.crawl;

import com.cse10.crawler.crawlControler.BasicCrawlController;

import javax.swing.*;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * Created by TharinduWijewardane on 2015-01-18.
 */
public abstract class CrawlTask extends SwingWorker<Void, Void> implements Observer {

    protected boolean done = false;

    protected Date startDate;
    protected Date endDate;
    protected int numberOfDates;
    protected int dateCount = 1;

    protected BasicCrawlController crawlController;

    public CrawlTask(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        numberOfDates = getDateDiff(startDate, endDate);
    }

    private int getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void stopCrawling() {
        if (crawlController != null) {
            crawlController.stopCrawl();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        int progress = 100 * ++dateCount / numberOfDates;
        setProgress(Math.min(progress, 100));
        System.out.println("CRAWLING " + arg.toString() + " of " + o.getClass() + " COMPLETED");
    }

}
