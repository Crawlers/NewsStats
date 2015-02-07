package com.cse10.crawler.crawlControler;

import com.cse10.article.NewYorkTimesArticle;
import com.cse10.crawler.paperCrawler.NewYorkTimesCrawler;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewYorkTimesCrawlControllerTest extends BasicCrawlTest{

    NewYorkTimesCrawlController newYorkTimesCrawlController;
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

        newYorkTimesCrawlController = new NewYorkTimesCrawlController();
        newYorkTimesCrawlController.setStartDate(date);
        newYorkTimesCrawlController.setEndDate(date);

        String tableName = DatabaseConstants.classToTableName.get(NewYorkTimesArticle.class);
        DatabaseHandler.executeUpdate("DELETE FROM " + tableName + " WHERE created_date = '" + dateString + "'");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCrawl() throws Exception {

        TestCase.assertEquals(true, DatabaseHandler.getRowCount(NewYorkTimesArticle.class, "createdDate", date) == 0);
        newYorkTimesCrawlController.crawl(NewYorkTimesCrawler.class);
        TestCase.assertEquals(true, DatabaseHandler.getRowCount(NewYorkTimesArticle.class, "createdDate", date) > 0);
    }
}
