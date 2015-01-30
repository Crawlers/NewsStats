package com.cse10.gui.task.crawl;

import com.cse10.article.CeylonTodayArticle;
import com.cse10.crawler.crawlControler.BasicCrawlController;
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

    public CeylonTodayCrawlTask() {
    }

    @Override
    protected Class getArticleClassType() {
        return CeylonTodayArticle.class;
    }

    @Override
    protected Class getCrawlerClassType() {
        return CeylonTodayCrawler.class;
    }

    @Override
    protected BasicCrawlController getCrawlController() {
        return new CeylonTodayCrawlController();
    }

}
