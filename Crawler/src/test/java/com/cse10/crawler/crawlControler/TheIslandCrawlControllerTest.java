package com.cse10.crawler.crawlControler;

import com.cse10.article.TheIslandArticle;
import com.cse10.crawler.paperCrawler.TheIslandCrawler;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TheIslandCrawlControllerTest extends BasicCrawlTest {

    TheIslandCrawlController theIslandCrawlController;
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

        theIslandCrawlController = new TheIslandCrawlController();
        theIslandCrawlController.setStartDate(date);
        theIslandCrawlController.setEndDate(date);

        String tableName = new DatabaseConstants().classToTableName.get(TheIslandArticle.class);
        DatabaseHandler.executeUpdate("DELETE FROM " + tableName + " WHERE created_date = '" + dateString + "'");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {

        TestCase.assertEquals(true, DatabaseHandler.getRowCount(TheIslandArticle.class, "createdDate", date) == 0);
        theIslandCrawlController.crawl(TheIslandCrawler.class);
        TestCase.assertEquals(true, DatabaseHandler.getRowCount(TheIslandArticle.class, "createdDate", date) > 0);
    }
}
