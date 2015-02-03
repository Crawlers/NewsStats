package com.cse10.gui.task.crawl;

import com.cse10.article.TheIslandArticle;
import com.cse10.crawler.crawlControler.BasicCrawlController;
import com.cse10.crawler.crawlControler.NewsFirstCrawlController;
import com.cse10.crawler.paperCrawler.TheIslandCrawler;

import java.util.Date;

/**
 * Created by TharinduWijewardane on 2015-01-10.
 */
public class TheIslandCrawlTask extends CrawlTask {

    public TheIslandCrawlTask(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    public TheIslandCrawlTask() {
    }

    @Override
    protected Class getArticleClassType() {
        return TheIslandArticle.class;
    }

    @Override
    protected Class getCrawlerClassType() {
        return TheIslandCrawler.class;
    }

    @Override
    protected BasicCrawlController getCrawlController() {
        return new NewsFirstCrawlController();
    }

}
