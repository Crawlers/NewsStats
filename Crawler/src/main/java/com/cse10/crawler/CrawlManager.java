package com.cse10.crawler;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */

import com.cse10.crawler.crawlControler.CeylonTodayCrawlController;
import com.cse10.crawler.paperCrawler.CeylonTodayCrawler;

public class CrawlManager {

    public static void main(String[] args) throws Exception {

//        new Thread() {
//            @Override
//            public void run() {
//                DailyMirrorCrawlController dailyMirrorCrawlController = new DailyMirrorCrawlController();
//                dailyMirrorCrawlController.setStartDate("2014-01-01");
//                dailyMirrorCrawlController.setEndDate("2014-02-01");
//                try {
//                    dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                TheIslandCrawlController theIslandCrawlController = new TheIslandCrawlController();
//                theIslandCrawlController.setStartDate("2014-01-01");
//                theIslandCrawlController.setEndDate("2014-02-01");
//                try {
//                    theIslandCrawlController.crawl(TheIslandCrawler.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//        new Thread() {
//            @Override
//            public void run() {
//                NewsFirstCrawlController newsFirstCrawlController = new NewsFirstCrawlController();
//                newsFirstCrawlController.setStartDate("2014-01-01");
//                newsFirstCrawlController.setEndDate("2014-02-01");
//                try {
//                    newsFirstCrawlController.crawl(NewsFirstCrawler.class);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

        new Thread() {
            @Override
            public void run() {
                CeylonTodayCrawlController ceylonTodayCrawlController = new CeylonTodayCrawlController();
                ceylonTodayCrawlController.setStartDate("2014-06-30");
                ceylonTodayCrawlController.setEndDate("2014-12-31");
                try {
                    ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
