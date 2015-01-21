package com.cse10.gui.task.crawl;

import com.cse10.crawler.crawlControler.TheIslandCrawlController;
import com.cse10.crawler.paperCrawler.TheIslandCrawler;

import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class TheIslandCrawlTask extends CrawlTask {

    public TheIslandCrawlTask(Date startDate, Date endDate) {
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

            crawlController = new TheIslandCrawlController();
            crawlController.setStartDate(startDate);
            crawlController.setEndDate(endDate);
            crawlController.addObserver(this);
            try {
                crawlController.crawl(TheIslandCrawler.class);
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
