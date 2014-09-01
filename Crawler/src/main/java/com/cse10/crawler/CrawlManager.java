package com.cse10.crawler;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */

import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;
import com.cse10.crawler.crawlControler.DailyMirrorCrawlController;
import com.cse10.crawler.crawlControler.NewsFirstCrawlController;
import com.cse10.crawler.paperCrawler.CeylonTodayCrawler;
import com.cse10.crawler.paperCrawler.DailyMirrorCrawler;
import com.cse10.crawler.paperCrawler.NewsFirstCrawler;

public class CrawlManager {

    public static void main(String[] args) throws Exception {

//        DailyMirrorCrawlController dailyMirrorCrawlController = new DailyMirrorCrawlController();
//        dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
//
//        NewsFirstCrawlController newsFirstCrawlController = new NewsFirstCrawlController();
//        newsFirstCrawlController.crawl(NewsFirstCrawler.class);
//
//        CeylonTodayCrawlController ceylonTodayCrawlController = new CeylonTodayCrawlController();
//        ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);

        new Thread() {
            @Override
            public void run() {
                DailyMirrorCrawlController dailyMirrorCrawlController = new DailyMirrorCrawlController();
                try {
                    dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                NewsFirstCrawlController newsFirstCrawlController = new NewsFirstCrawlController();
                try {
                    newsFirstCrawlController.crawl(NewsFirstCrawler.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                CeylonTodayCrawlController ceylonTodayCrawlController = new CeylonTodayCrawlController();
                try {
                    ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

