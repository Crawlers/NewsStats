package com.cse10.crawler.crawlControler;

import com.cse10.crawler.paperCrawler.CeylonTodayCrawler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class CeylonTodayCrawlControllerTest {

    CeylonTodayCrawlController ceylonTodayCrawlController;

    @Before
    public void setUp() throws Exception {
        ceylonTodayCrawlController = new CeylonTodayCrawlController();
        ceylonTodayCrawlController.setStartDate(new Date());
        ceylonTodayCrawlController.setEndDate(new Date());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {
        ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);
    }
}
