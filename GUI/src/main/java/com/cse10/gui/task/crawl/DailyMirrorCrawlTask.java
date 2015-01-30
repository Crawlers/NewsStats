package com.cse10.gui.task.crawl;

import com.cse10.article.DailyMirrorArticle;
import com.cse10.crawler.crawlControler.BasicCrawlController;
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

    @Override
    protected Class getCrawlerClassType() {
        return DailyMirrorCrawler.class;
    }

    @Override
    protected BasicCrawlController getCrawlController() {
        return new DailyMirrorCrawlController();
    }

}
