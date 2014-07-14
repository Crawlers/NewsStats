package com.cse10.crawler;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */

import com.cse10.crawler.crawlControler.DailyMirrorCrawlController;
import com.cse10.crawler.paperCrawler.DailyMirrorCrawler;

public class CrawlManager {

    public static void main(String[] args) throws Exception {
        DailyMirrorCrawlController dailyMirrorCrawlController = new DailyMirrorCrawlController();
        dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
    }
}

