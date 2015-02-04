package com.cse10.crawler.crawlControler;

import com.cse10.crawler.paperCrawler.DailyMirrorCrawler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DailyMirrorCrawlControllerTest {

    DailyMirrorCrawlController dailyMirrorCrawlController;

    @Before
    public void setUp() throws Exception {

        dailyMirrorCrawlController = new DailyMirrorCrawlController();
        dailyMirrorCrawlController.setStartDate("2014-12-01");
        dailyMirrorCrawlController.setEndDate("2014-12-01");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {
        dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
    }
}