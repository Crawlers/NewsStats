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
 * Created by TharinduWijewardane on 17.07.2014.
 */
public class NewsFirstCrawlController extends BasicCrawlController {

    public static String current_path;
    final String FROM_DATE = "2014-01-01";
    final String TO_DATE = "2014-07-01";

    public <T extends WebCrawler> void crawl(final Class<T> _c) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startingDate = sdf.parse(FROM_DATE);
        startingDate = DateHandler.getFromDateToResume(startingDate, "article_news_first");  // Start date
        Calendar c = Calendar.getInstance();
        c.setTime(startingDate);

        while (c.getTime().compareTo(sdf.parse(TO_DATE)) <= 0) {

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1; //java defines january as 0

            for (int pageNum = 1; pageNum < 100; pageNum++) { // there are multiple pages for a month. assuming max < 100

            /*
         * Instantiate the controller for this crawl.
         */
                PageFetcher pageFetcher = new PageFetcher(getConfig());
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

                CrawlController controller = new CrawlController(getConfig(), pageFetcher, robotstxtServer);

                String url = "http://newsfirst.lk/english/" + year + "/" + (month < 10 ? ("0" + month) : (month)); // make the month always 2 digits
                current_path = "/english/" + year + "/" + (month < 10 ? ("0" + month) : (month));

                if (pageNum > 1) {
                    url += "/page/" + pageNum;
                }

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
                c.add(Calendar.MONTH, 1);  // number of months to add

            }

        }
    }
}
