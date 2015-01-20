package com.cse10.gui.task.crawl;

import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;

import java.util.Date;
import java.util.Observable;
import java.util.Random;

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
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);

            CeylonTodayCrawlController ceylonTodayCrawlController = new CeylonTodayCrawlController();
            ceylonTodayCrawlController.setStartDate("2014-06-30");
            ceylonTodayCrawlController.setEndDate("2014-12-31");
            ceylonTodayCrawlController.addObserver(this);
            try {
//                ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    @Override
    public void update(Observable o, Object arg) {

    }
}
