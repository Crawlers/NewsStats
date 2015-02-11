package com.cse10.crawler.crawlControler;

import com.cse10.crawler.paperCrawler.CeylonTodayNewCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by TharinduWijewardane on 2015-02-07.
 */
public class CeylonTodayNewCrawlController extends BasicCrawlController {

    public static void main(String[] args) {
        try {
            new CeylonTodayNewCrawlController().crawl(CeylonTodayNewCrawler.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception {

        for (int i = 29; i < 143; i++) {

            /*
         * Instantiate the controller for this crawl.
         */
            PageFetcher pageFetcher = new PageFetcher(getConfig());
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

            controller = new CrawlController(getConfig(), pageFetcher, robotstxtServer);
            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */

            String url = "http://www.ceylontoday.lk/16-" + i + "-news-list-latest-news.html";

            controller.addSeed(url);
            logger.info("crawling " + url);
            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(_c, 1);

            if (crawlingStopped) { //if stopped from calling class
                return;
            }
        }
    }
}
