package com.cse10.crawler.crawlControler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CeylonTodayCrawlControllerTest extends BasicCrawlTest {

    CeylonTodayCrawlController ceylonTodayCrawlController;
    String dateString = "2015-01-01";
    Date date;

    @Before
    public void setUp() throws Exception {

        changeDB();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            date = new Date(); // set today if fails
            e.printStackTrace();
        }

        ceylonTodayCrawlController = new CeylonTodayCrawlController();
        ceylonTodayCrawlController.setStartDate(date);
        ceylonTodayCrawlController.setEndDate(date);

//        String tableName = new DatabaseConstants().classToTableName.get(CeylonTodayArticle.class);
//        DatabaseHandler.executeUpdate("DELETE FROM " + tableName + " WHERE created_date = '" + dateString + "'");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {

//        TestCase.assertTrue(DatabaseHandler.getRowCount(CeylonTodayArticle.class, "createdDate", date) == 0);
//        ceylonTodayCrawlController.crawl(CeylonTodayCrawler.class);
//        TestCase.assertTrue(DatabaseHandler.getRowCount(CeylonTodayArticle.class, "createdDate", date) > 0);
    }
}
