package com.cse10.gui.task.crawl;

import com.cse10.crawler.crawlControler.BasicCrawlController;
import com.cse10.database.DatabaseHandler;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    protected int dateCount;

    protected BasicCrawlController crawlController;

    public CrawlTask() {
        setDateFromDB();
        numberOfDates = getDateDiff(startDate, endDate) + 1;
    }

    public CrawlTask(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        numberOfDates = getDateDiff(startDate, endDate) + 1;
    }

    private void setDateFromDB() {
        startDate = DatabaseHandler.getLatestDate(getArticleClassType());
        if (startDate == null) { // if not set by DB
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDate = sdf.parse("2014-01-01");
            } catch (ParseException e) {
                startDate = new Date(); // set today if fails
                e.printStackTrace();
            }
        }

        endDate = new Date(); // set today
        //todo remove following
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            endDate = sdf.parse("2012-01-11");
        } catch (ParseException e) {
            endDate = new Date(); // set today if fails
            e.printStackTrace();
        }
    }

    protected abstract Class getArticleClassType(); // tobe implemented in subclasses accordingly

    private int getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void stopCrawling() {
        if (crawlController != null) {
            crawlController.stopCrawl();
        }
    }

    protected abstract Class getCrawlerClassType(); // tobe implemented in subclasses accordingly

    protected abstract BasicCrawlController getCrawlController(); // tobe implemented in subclasses accordingly

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("in background");

            //Initialize progress property.
            setProgress(1);

            crawlController = getCrawlController();
            crawlController.setStartDate(startDate);
            crawlController.setEndDate(endDate);
            crawlController.addObserver(this);
            try {
                crawlController.crawl(getCrawlerClassType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            crawlController.deleteObserver(this);
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

    @Override
    public void update(Observable o, Object arg) {
        int progress = 100 * ++dateCount / numberOfDates;
        setProgress(Math.min(progress, 100));
        System.out.println("CRAWLING " + arg.toString() + " of " + o.getClass() + " COMPLETED");
    }

}
