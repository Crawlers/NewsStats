package com.cse10.gui.task.crawl;

import com.cse10.article.NewsFirstArticle;
import com.cse10.crawler.crawlControler.BasicCrawlController;
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

    @Override
    protected Class getCrawlerClassType() {
        return NewsFirstCrawler.class;
    }

    @Override
    protected BasicCrawlController getCrawlController() {
        return new NewsFirstCrawlController();
    }

}
