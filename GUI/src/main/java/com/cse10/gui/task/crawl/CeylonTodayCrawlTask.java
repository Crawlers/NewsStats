package com.cse10.gui.task.crawl;

import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;
import com.cse10.crawler.paperCrawler.CeylonTodayCrawler;

import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class CeylonTodayCrawlTask extends CrawlTask {

    public CeylonTodayCrawlTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        if (!done) {
            System.out.println("in background");

            //Initialize progress property.
            setProgress(1);

            crawlController = new CeylonTodayCrawlController();
            crawlController.setStartDate("2014-06-30");
            crawlController.setEndDate("2014-12-31");
            crawlController.addObserver(this);
            try {
                crawlController.crawl(CeylonTodayCrawler.class);
            } catch (Exception e) {
                e.printStackTrace();
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
