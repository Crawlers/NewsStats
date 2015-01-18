package com.cse10.crawler.crawlControler;

/**
 * Created by Sampath on 13.07.2014
 */

import com.cse10.crawler.DateHandler;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TheIslandCrawlController extends BasicCrawlController {

    /*
    * Web site of the Island news paper uses cookies to show n ews articles
    * Crawler4j doesn't handle cookies
    * As a solution a php proxy to handle cookies were implemented  (https://github.com/sampathLiyanage/phpCurlCookies.git)
    * That php app acts as a middleware between crawler4j and the website
    * put the theIsland directory inside www folder and update the address of localhostProxyUrl below accordingly
    * Make sure the web server runs, and you can access the localhostProxyUrl from the browser before crawling
    * */
    final String localhostProxyUrl = "http://localhost:8080/theIsland/";

    public static String current_date;


    public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception {

        if (startDate == null || endDate == null) {
            System.out.println("Error: You should set start and end dates");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startingDate = sdf.parse(startDate);
        startingDate = DateHandler.getFromDateToResume(startingDate, "article_the_island");  // Start date
        Calendar c = Calendar.getInstance();
        c.setTime(startingDate);

        while (c.getTime().compareTo(sdf.parse(endDate)) <= 0) {
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
