package com.cse10.gui.task.crawl;

import com.cse10.article.DailyMirrorArticle;
import com.cse10.crawler.crawlControler.DailyMirrorCrawlController;
import com.cse10.crawler.paperCrawler.DailyMirrorCrawler;

import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class DailyMirrorCrawlTask extends CrawlTask {

    public DailyMirrorCrawlTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    public DailyMirrorCrawlTask() {
    }

    @Override
    protected Class getArticleClassType() {
        return DailyMirrorArticle.class;
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

            crawlController = new DailyMirrorCrawlController();
            crawlController.setStartDate(startDate);
            crawlController.setEndDate(endDate);
            crawlController.addObserver(this);
            try {
                crawlController.crawl(DailyMirrorCrawler.class);
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
