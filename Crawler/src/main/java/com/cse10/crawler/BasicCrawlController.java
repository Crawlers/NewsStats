package com.cse10.crawler;

/**
 * Created by TharinduWijewardane on 29.06.2014.
 */
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BasicCrawlController {

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.out.println("Needed parameters: ");
//            System.out.println("\t rootFolder (it will contain intermediate crawl data)");
//            System.out.println("\t numberOfCralwers (number of concurrent threads)");
//            return;
//        }

                /*
                 * crawlStorageFolder is a folder where intermediate crawl data is
                 * stored.
                 */
        String crawlStorageFolder = "/home/tharindu/data/crawl/root"; //args[0];

                /*
                 * numberOfCrawlers shows the number of concurrent threads that should
                 * be initiated for crawling.
                 */
        int numberOfCrawlers = 2; //Integer.parseInt(args[1]);

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);

                /*
                 * Be polite: Make sure that we don't send more than 1 request per
                 * second (1000 milliseconds between requests).
                 */
        config.setPolitenessDelay(50);

                /*
                 * You can set the maximum crawl depth here. The default value is -1 for
                 * unlimited depth
                 */
        config.setMaxDepthOfCrawling(4);

                /*
                 * You can set the maximum number of pages to crawl. The default value
                 * is -1 for unlimited number of pages
                 */
        config.setMaxPagesToFetch(10);

                /*
                 * Do you need to set a proxy? If so, you can use:
                 */
//        config.setProxyHost("cache.mrt.ac.lk");
//        config.setProxyPort(3128);
                 /*
                 * If your proxy also needs authentication:
                 * config.setProxyUsername(username); config.getProxyPassword(password);
                 * --------Isn't it proxy.setProxyPassword(password) ?---------
                 */

                /*
                 * This config parameter can be used to set your crawl to be resumable
                 * (meaning that you can resume the crawl from a previously
                 * interrupted/crashed crawl). Note: if you enable resuming feature and
                 * want to start a fresh crawl, you need to delete the contents of
                 * rootFolder manually.
                 */
        config.setResumableCrawling(false);

        System.out.println(config.toString()); // print config

                /*
                 * Instantiate the controller for this crawl.
                 */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

                /*
                 * For each crawl, you need to add some seed urls. These are the first
                 * URLs that are fetched and then the crawler starts following links
                 * which are found in these pages
                 */
        controller.addSeed("http://www.dailymirror.lk/");
//        controller.addSeed("http://www.island.lk/");

//        controller.addSeed("http://www.ics.uci.edu/");
//        controller.addSeed("http://www.ics.uci.edu/~lopes/");
//        controller.addSeed("http://www.ics.uci.edu/~welling/");

                /*
                 * Start the crawl. This is a blocking operation, meaning that your code
                 * will reach the line after this only when crawling is finished.
                 */
        controller.start(BasicCrawler.class, numberOfCrawlers);
    }
}

