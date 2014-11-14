package com.cse10.crawler.crawlControler;

import com.cse10.crawler.DateHandler;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tharindu on 2014-11-13.
 */
public class HiruNewsCrawlController extends BasicCrawlController {

    final String FROM_DATE = "2014-01-01";
    final String TO_DATE = "2014-10-31";
    public static Calendar cal;

    public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startingDate = sdf.parse(FROM_DATE);
        startingDate = DateHandler.getFromDateToResume(startingDate, "article_hiru_news");  // Start date
        Calendar c = Calendar.getInstance();
        c.setTime(startingDate);

        while (c.getTime().compareTo(sdf.parse(TO_DATE)) <= 0) {

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1; //java defines january as 0
            int date = c.get(Calendar.DATE);
            cal = c;

            for (int pageNum = 1; pageNum <= 50; pageNum++) { // there are multiple pages for a month. assuming max <= 50

            /*
         * Instantiate the controller for this crawl.
         */
                PageFetcher pageFetcher = new PageFetcher(getConfig());
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

                CrawlController controller = new CrawlController(getConfig(), pageFetcher, robotstxtServer);

                String url = "http://www.hirunews.lk/news-archives.php?m=" + (month < 10 ? ("0" + month) : (month)) + "&pageID=" + pageNum + "&y=" + year; // make the month always 2 digits

                /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
                controller.addSeed(url);
                System.out.println("crawling " + url);
            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
                controller.start(_c, 1);
//                System.out.println("sleeping for 60 s");
//                Thread.sleep(60000);

            }

            c.add(Calendar.MONTH, 1);  // number of months to add

        }
    }

}
