package com.cse10.gui.task.crawl;

import com.cse10.article.NewsFirstArticle;
import com.cse10.crawler.crawlControler.NewsFirstCrawlController;
import com.cse10.crawler.paperCrawler.NewsFirstCrawler;

import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class NewsFirstCrawlTask extends CrawlTask {

    public NewsFirstCrawlTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    public NewsFirstCrawlTask() {
    }

    @Override
    protected Class getArticleClassType() {
        return NewsFirstArticle.class;
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

            crawlController = new NewsFirstCrawlController();
            crawlController.setStartDate(startDate);
            crawlController.setEndDate(endDate);
            crawlController.addObserver(this);
            try {
                crawlController.crawl(NewsFirstCrawler.class);
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
