package com.cse10.crawler.crawlControler;

import com.cse10.article.DailyMirrorArticle;
import com.cse10.crawler.paperCrawler.DailyMirrorCrawler;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyMirrorCrawlControllerTest {

    DailyMirrorCrawlController dailyMirrorCrawlController;
    String dateString = "2014-10-17";
    Date date;

    @Before
    public void setUp() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            date = new Date(); // set today if fails
            e.printStackTrace();
        }

        dailyMirrorCrawlController = new DailyMirrorCrawlController();
        dailyMirrorCrawlController.setStartDate(date);
        dailyMirrorCrawlController.setEndDate(date);

        String tableName = new DatabaseConstants().classToTableName.get(DailyMirrorArticle.class);
        DatabaseHandler.executeUpdate("DELETE FROM " + tableName + " WHERE created_date = '" + dateString + "'");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {

        TestCase.assertEquals(true, DatabaseHandler.getRowCount(DailyMirrorArticle.class, "createdDate", date) == 0);
        dailyMirrorCrawlController.crawl(DailyMirrorCrawler.class);
        TestCase.assertEquals(true, DatabaseHandler.getRowCount(DailyMirrorArticle.class, "createdDate", date) > 0);
    }
}
