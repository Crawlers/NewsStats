package com.cse10.crawler.crawlControler;

/**
 * Created by Sampath on 13.07.2014
 */

import com.cse10.crawler.DateHandler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TheIslandCrawlController extends BasicCrawlController {
    final String localhostProxyUrl = "http://localhost:8080/theIsland/";
    final String FROM_DATE = "2013-01-01";
    final String TO_DATE = "2013-12-31";
    public static String current_date;


    public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startingDate = sdf.parse(FROM_DATE);
        startingDate = DateHandler.getFromDateToResume(startingDate, "article_the_island");  // Start date
        Calendar c = Calendar.getInstance();
        c.setTime(startingDate);

        while (c.getTime().compareTo(sdf.parse(TO_DATE)) <= 0) {
            /*
         * Instantiate the controller for this crawl.
         */
            PageFetcher pageFetcher = new PageFetcher(getConfig());
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

            CrawlController controller = new CrawlController(getConfig(), pageFetcher, robotstxtServer);
            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1; //java defines january as 0
            int date = c.get(Calendar.DATE);
            current_date = sdf.format(c.getTime());
            String url = localhostProxyUrl + "?newsfordate=" + date + "/" + month + "/" + year;

            controller.addSeed(url);
            System.out.println("crawling " + url);
            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(_c, 1);
            c.add(Calendar.DATE, 1);  // number of days to add
        }
    }
}
