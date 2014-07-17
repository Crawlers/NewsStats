package com.cse10.crawler;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */

import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;
import com.cse10.crawler.paperCrawler.CeylonTodayCrawler;

public class CrawlManager {

    public static void main(String[] args) throws Exception {
//        DailyMirrorCrawlController dailyMirrorCrawlController = new DailyMirrorCrawlController();
//        dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
        CeylonTodayCrawlController ceylonTodayCrawlController = new CeylonTodayCrawlController();
        ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);
    }
}

