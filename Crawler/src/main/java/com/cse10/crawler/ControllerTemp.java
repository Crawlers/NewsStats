package com.cse10.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */
public class ControllerTemp {
//    public static void main111(String[] args) throws Exception {
//        String crawlStorageFolder = "/home/tharindu/data/crawl/root";
//        int numberOfCrawlers = 7;
//
//        CrawlConfig config = new CrawlConfig();
//        config.setCrawlStorageFolder(crawlStorageFolder);
//        System.out.println(config.toString());
//
//                /*
//                 * Instantiate the controller for this crawl.
//                 */
//        PageFetcher pageFetcher = new PageFetcher(config);
//        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
//        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
//        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
//
//                /*
//                 * For each crawl, you need to add some seed urls. These are the first
//                 * URLs that are fetched and then the crawler starts following links
//                 * which are found in these pages
//                 */
////        controller.addSeed("http://www.dailymirror.lk/");
//        controller.addSeed("http://www.ics.uci.edu/~welling/");
//        controller.addSeed("http://www.ics.uci.edu/~lopes/");
//        controller.addSeed("http://www.ics.uci.edu/");
//
//                /*
//                 * Start the crawl. This is a blocking operation, meaning that your code
//                 * will reach the line after this only when crawling is finished.
//                 */
//        controller.start(MyCrawler.class, numberOfCrawlers);
//    }
}
